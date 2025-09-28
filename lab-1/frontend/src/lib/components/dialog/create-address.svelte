<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Address } from '$lib/types/address';
	import type { Location } from '$lib/types/location';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
	import SelectLocationDialog from '$lib/components/dialog/select-location.svelte';
	import CreateLocationDialog from '$lib/components/dialog/create-location.svelte';

	let open = $state(false);
	let { expose = $bindable({}), callback = (addr: Address) => {} } = $props();

	let street = $state('');
	let zipCode = $state('');
	let townId = $state<number | null>(null);
	let errorMessage = $state('');
	let selectLocationApi = $state<any>({});
	let createLocationApi = $state<any>({});

	expose.toggle = () => {
		street = '';
		zipCode = '';
		townId = null;
		open = true;
	};

	const validate = () => {
		if (!street) {
			errorMessage = 'Введите улицу';
			return false;
		}
		if (zipCode && zipCode.length > 24) {
			errorMessage = 'Почтовый код должен быть короче 24 символов';
			return false;
		}
		if (!townId) {
			errorMessage = 'Выберите город';
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
			const resp = await http.post<Address>('/management/address', {
				street: street.trim(),
				zipCode: zipCode ? zipCode.trim() : null,
				townId: townId
			});
			const addr = resp.data;
			callback(addr);
			open = false;
		} catch (ex) {
			console.error('Error while adding address', ex);
			toast('Ошибка при добавлении адреса');
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Создание адреса</Dialog.Title>
			<Dialog.Description>Создать адрес и добавить его в хранилище</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="id" class="text-right">ID</Label>
				<Input id="id" value={'-'} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="userId" class="text-right">ID владельца</Label>
				<Input id="userId" value={'-'} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="street" class="text-right">Улица</Label>
				<Input id="street" bind:value={street} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="zipCode" class="text-right">Почтовый код</Label>
				<Input id="zipCode" bind:value={zipCode} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Город</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-4"
						variant="secondary"
						onclick={selectLocationApi.toggle}
					>
						{#if townId}
							Локация #{townId}
						{:else}
							Выбрать
						{/if}
					</Button>
					<Button id="town" class="col-span-1" variant="outline" onclick={createLocationApi.toggle}>
						<PlusIcon />
					</Button>
				</div>
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

<SelectLocationDialog
	bind:expose={selectLocationApi}
	callback={(loc: Location) => {
		townId = loc?.id ?? null;
	}}
/>
<CreateLocationDialog
	bind:expose={createLocationApi}
	callback={(loc: Location) => {
		townId = loc?.id ?? null;
	}}
/>
