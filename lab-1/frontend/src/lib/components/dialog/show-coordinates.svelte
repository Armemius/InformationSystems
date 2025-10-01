<script lang="ts">
	import http from '$lib/api/http';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Coordinates } from '$lib/types/coordinates';
	import { toast } from 'svelte-sonner';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';

	let open = $state(false);
	let coordinates = $state<Coordinates | null>(null);
	let { expose = $bindable({}) } = $props();

	expose.toggle = (coords: Coordinates) => {
		coordinates = coords;
		open = true;
	};

	expose.toggleById = async (id: number) => {
		try {
			const resp = await http.get<Coordinates>(`/management/coordinates/${id}`);
			coordinates = resp.data;
			open = true;
		} catch (ex) {
			toast('Ошибка загрузки координат');
			console.error('Error while fetching coordinates', ex);
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Просмотр координат</Dialog.Title>
			<Dialog.Description>
				Отобразить текущую координату и её состояние в хранилище
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
				<Label for="x" class="text-right">X</Label>
				<Input id="x" type="number" value={coordinates?.x} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="y" class="text-right">Y</Label>
				<Input id="y" type="number" value={coordinates?.y} disabled class="col-span-3" />
			</div>
		</div>
	</Dialog.Content>
</Dialog.Root>
