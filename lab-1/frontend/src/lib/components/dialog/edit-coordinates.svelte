<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Coordinates } from '$lib/types/coordinates';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';

	let open = $state(false);
	let coordinates = $state<Coordinates | null>(null);
	let { expose = $bindable({}) } = $props();

	let x = $state('');
	let y = $state('');
	let errorMessage = $state('');

	expose.toggle = (coords: Coordinates) => {
		coordinates = coords;
		open = true;
	};

	$effect(() => {
		x = coordinates?.x?.toString() ?? '';
		y = coordinates?.y?.toString() ?? '';
	});

	const validate = () => {
		if (!y) {
			errorMessage = 'Заполните значение Y';
			return false;
		}
		if (!x) {
			errorMessage = 'Заполните значение X';
			return false;
		}
		const xValue = Number(x.toString().replace(',', '.'));
		if (isNaN(xValue)) {
			errorMessage = 'Значение X должно быть числом';
			return false;
		}
		if (xValue <= -469) {
			errorMessage = 'Значение X должно быть больше -469';
			return false;
		}
		const yValue = Number(y.toString().replace(',', '.'));
		if (isNaN(yValue)) {
			errorMessage = 'Значение Y должно быть числом';
			return false;
		}
		if (yValue <= -440) {
			errorMessage = 'Значение Y должно быть больше -440';
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
			await http.patch(`/management/coordinates/${coordinates?.id}`, {
				x: x ? Number(x.toString().replace(',', '.')) : null,
				y: y ? Number(y.toString().replace(',', '.')) : null
			});
			open = false;
		} catch (ex) {
			console.error('Error while updating coordinates', ex);
			toast('Ошибка при обновлении координат');
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Обновление координат</Dialog.Title>
			<Dialog.Description>
				Изменить существующую координату и обновить её состояние в хранилище
			</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="id" class="text-right">ID</Label>
				<Input id="id" value={coordinates?.id} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="userId" class="text-right">ID владельца</Label>
				<Input id="userId" value={coordinates?.ownerId} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="x" class="text-right">
					<span>
						X<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="x" type="number" bind:value={x} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="y" class="text-right">
					<span>
						Y<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="y" type="number" bind:value={y} class="col-span-3" />
			</div>
		</div>

		<Dialog.Footer>
			<div class="grid w-full grid-cols-2">
				<div class="flex items-center justify-start text-xs text-red-800">{errorMessage}</div>
				<Button type="submit" onclick={submit}>Сохранить изменения</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
