<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Organization } from '$lib/types/organization';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
	import SelectOrganizationDialog from '$lib/components/dialog/select-organization.svelte';
	import SelectAddressDialog from '$lib/components/dialog/select-address.svelte';
	import CreateAddressDialog from '$lib/components/dialog/create-address.svelte';
	import type { CreateApi, SelectApi } from '$lib/types/togglers';

	let open = $state(false);
	let { expose = $bindable({}) } = $props();

	let name = $state('');
	let addressId = $state<number | null>(null);
	let firstOrgId = $state<number | null>(null);
	let secondOrgId = $state<number | null>(null);
	let errorMessage = $state('');

	let selectFirstOrganizationApi = $state<SelectApi>({});
	let selectSecondOrganizationApi = $state<SelectApi>({});
	let selectAddressDialogApi = $state<SelectApi>({});
	let createAddressDialogApi = $state<CreateApi>({});

	expose.toggle = () => {
		name = '';
		addressId = null;
		firstOrgId = null;
		secondOrgId = null;
		errorMessage = '';
		open = true;
	};

	const validate = () => {
		if (!name) {
			errorMessage = 'Заполните имя';
			return false;
		}
		if (!addressId) {
			errorMessage = 'Выберите адрес';
			return false;
		}
		if (!firstOrgId || !secondOrgId) {
			errorMessage = 'Выберите обе организации';
			return false;
		}
		if (firstOrgId === secondOrgId) {
			errorMessage = 'Выберите разные организации';
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
			await http.post<Organization>(
				'/management/merge',
				{},
				{
					params: {
						newName: name,
						addressId: addressId,
						firstOrgId: firstOrgId,
						secondOrgId: secondOrgId
					}
				}
			);
			open = false;
		} catch (ex) {
			console.error('Error while absorbing organization', ex);
			toast('Ошибка при поглощении организации');
		}
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Слияние организаций</Dialog.Title>
			<Dialog.Description>
				Объединить две организации, создав новую с указанным именем и адресом
			</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">
					<span>
						Имя<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="name" bind:value={name} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">
					<span>
						Адрес<span class="text-red-500">*</span>
					</span>
				</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-4"
						variant="secondary"
						onclick={selectAddressDialogApi.toggle}
					>
						{#if addressId}
							Адрес #{addressId}
						{:else}
							Выбрать
						{/if}
					</Button>
					<Button
						id="town"
						class="col-span-1"
						variant="outline"
						onclick={createAddressDialogApi.toggle}
					>
						<PlusIcon />
					</Button>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">
					<span>
						Орг. #1<span class="text-red-500">*</span>
					</span>
				</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-5"
						variant="secondary"
						onclick={selectFirstOrganizationApi.toggle}
					>
						{#if firstOrgId}
							Организация #{firstOrgId}
						{:else}
							Выбрать
						{/if}
					</Button>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">
					<span>
						Орг. #2<span class="text-red-500">*</span>
					</span>
				</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-5"
						variant="secondary"
						onclick={selectSecondOrganizationApi.toggle}
					>
						{#if secondOrgId}
							Организация #{secondOrgId}
						{:else}
							Выбрать
						{/if}
					</Button>
				</div>
			</div>
		</div>

		<Dialog.Footer>
			<div class="grid w-full grid-cols-2">
				<div class="flex items-center justify-start text-xs text-red-800">{errorMessage}</div>
				<Button type="submit" onclick={submit}>Выполнить операцию</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>

<SelectAddressDialog
	bind:expose={selectAddressDialogApi}
	callback={(addr) => {
		addressId = addr?.id ?? null;
	}}
/>
<CreateAddressDialog
	bind:expose={createAddressDialogApi}
	callback={(addr) => {
		addressId = addr?.id ?? null;
	}}
/>
<SelectOrganizationDialog
	bind:expose={selectFirstOrganizationApi}
	callback={(org) => {
		firstOrgId = org?.id ?? null;
	}}
/>
<SelectOrganizationDialog
	bind:expose={selectSecondOrganizationApi}
	callback={(org) => {
		secondOrgId = org?.id ?? null;
	}}
/>
