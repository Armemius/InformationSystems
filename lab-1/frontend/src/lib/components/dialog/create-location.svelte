<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';

	let open = $state(false);
	let { expose = $bindable({}), callback = (loc: Location) => {} } = $props();

	let x = $state('');
	let y = $state('');
	let z = $state('');
	let errorMessage = $state('');

	expose.toggle = () => {
		x = '';
		y = '';
		z = '';
		open = true;
	};

	const validate = () => {
		if (!y) {
			errorMessage = 'Заполните значение Y';
			return false;
		}
		if (!x) {
			errorMessage = 'Заполните значение X';
			return false;
		}
		if (!z) {
			errorMessage = 'Заполните значение Z';
			return false;
		}
		const xValue = Number(x.toString().replace(',', '.'));
		if (isNaN(xValue)) {
			errorMessage = 'Значение X должно быть числом';
			return false;
		}
		const yValue = Number(y.toString().replace(',', '.'));
		if (isNaN(yValue)) {
			errorMessage = 'Значение Y должно быть числом';
			return false;
		}
		const zValue = Number(z.toString().replace(',', '.'));
		if (isNaN(zValue)) {
			errorMessage = 'Значение Z должно быть числом';
			return false;
		}

		return true;
	};

	const submit = async () => {
		if (!validate()) {
			return;
		}
		errorMessage = '';

		try {
			const resp = await http.post<Location>('/management/location', {
				x: x ? Number(x.toString().replace(',', '.')) : null,
				y: y ? Number(y.toString().replace(',', '.')) : null,
				z: z ? Number(z.toString().replace(',', '.')) : null
			});
			const loc = resp.data;
			callback(loc);
			open = false;
		} catch (ex) {
			console.error('Error while updating location', ex);
			toast('Ошибка при обновлении локации');
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Создание локации</Dialog.Title>
			<Dialog.Description>Создать локацию и добавить её в хранилище</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="x" class="text-right">X</Label>
				<Input id="x" type="number" bind:value={x} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="y" class="text-right">Y</Label>
				<Input id="y" type="number" bind:value={y} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="z" class="text-right">Z</Label>
				<Input id="z" type="number" bind:value={z} class="col-span-3" />
			</div>
		</div>

		<Dialog.Footer>
			<div class="grid w-full grid-cols-2">
				<div class="flex items-center justify-start text-xs text-red-800">{errorMessage}</div>
				<Button type="submit" onclick={submit}>Добавить</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
