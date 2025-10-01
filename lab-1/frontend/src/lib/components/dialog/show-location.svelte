<script lang="ts">
	import http from '$lib/api/http';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Location } from '$lib/types/location';
	import { toast } from 'svelte-sonner';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';

	let open = $state(false);
	let location = $state<Location | null>(null);
	let { expose = $bindable({}) } = $props();

	expose.toggle = (loc: Location) => {
		location = loc;
		open = true;
	};

	expose.toggleById = async (id: number) => {
		try {
			const resp = await http.get<Location>(`/management/location/${id}`);
			location = resp.data;
			open = true;
		} catch (ex) {
			toast('Ошибка загрузки локации');
			console.error('Error while fetching location', ex);
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Просмотр локации</Dialog.Title>
			<Dialog.Description>Отобразить текущую локацию и её состояние в хранилище</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="id" class="text-right">ID</Label>
				<Input id="id" value={location?.id} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="userId" class="text-right">ID владельца</Label>
				<Input id="userId" value={location?.ownerId} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="x" class="text-right">X</Label>
				<Input id="x" type="number" value={location?.x} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="y" class="text-right">Y</Label>
				<Input id="y" type="number" value={location?.y} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="z" class="text-right">Z</Label>
				<Input id="z" type="number" value={location?.z} disabled class="col-span-3" />
			</div>
		</div>
	</Dialog.Content>
</Dialog.Root>
