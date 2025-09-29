<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Address } from '$lib/types/address';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import ShowLocationDialog from '$lib/components/dialog/show-location.svelte';
	import type { ShowApi } from '$lib/types/togglers';
	import type { Location } from '$lib/types/location';

	let open = $state(false);
	let { expose = $bindable({}) } = $props();

	let id = $state('');
	let ownerId = $state('');
	let street = $state('');
	let zipCode = $state('');
	let townId = $state<number | null>(null);
	let showLocationApi = $state<ShowApi<Location>>({});

	expose.toggle = (addr: Address) => {
		if (addr.id) {
			id = String(addr.id);
		} else {
			id = '';
		}
		if (addr.ownerId) {
			ownerId = String(addr.ownerId);
		} else {
			ownerId = '';
		}
		street = addr.street;
		zipCode = addr.zipCode ?? '-';
		townId = addr.townId;
		open = true;
	};

	expose.toggleById = async (addrId: number) => {
		try {
			const resp = await http.get<Address>(`/management/address/${addrId}`);
			const addr = resp.data;
			if (addr.id) {
				id = String(addr.id);
			} else {
				id = '';
			}
			if (addr.ownerId) {
				ownerId = String(addr.ownerId);
			} else {
				ownerId = '';
			}
			street = addr.street;
			zipCode = addr.zipCode ?? '-';
			townId = addr.townId;
			open = true;
		} catch (ex) {
			toast('Ошибка загрузки адреса');
			console.error('Error while fetching address', ex);
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Просмотр адреса</Dialog.Title>
			<Dialog.Description>Отобразить текущий адрес и его состояние в хранилище</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="id" class="text-right">ID</Label>
				<Input id="id" value={id ?? '-'} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="userId" class="text-right">ID владельца</Label>
				<Input id="userId" value={ownerId ?? '-'} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="street" class="text-right">Улица</Label>
				<Input id="street" bind:value={street} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="zipCode" class="text-right">Почтовый код</Label>
				<Input id="zipCode" bind:value={zipCode} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Город</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-5"
						variant="outline"
						onclick={() => {
							if (showLocationApi.toggleById && townId) {
								showLocationApi.toggleById(townId);
							}
						}}
					>
						{#if townId}
							Локация #{townId}
						{:else}
							Не выбран
						{/if}
					</Button>
				</div>
			</div>
		</div>
	</Dialog.Content>
</Dialog.Root>

<ShowLocationDialog bind:expose={showLocationApi} />
