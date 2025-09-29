<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Organization } from '$lib/types/organization';
	import Button from '../ui/button/button.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import SelectOrganizationDialog from '$lib/components/dialog/select-organization.svelte';
	import type { SelectApi } from '$lib/types/togglers';

	let open = $state(false);
	let { expose = $bindable({}) } = $props();

	let firstOrgId = $state<number | null>(null);
	let secondOrgId = $state<number | null>(null);
	let errorMessage = $state('');

	let selectFirstOrganizationApi = $state<SelectApi>({});
	let selectSecondOrganizationApi = $state<SelectApi>({});

	expose.toggle = () => {
		firstOrgId = null;
		secondOrgId = null;
		errorMessage = '';
		open = true;
	};

	const validate = () => {
		if (!firstOrgId) {
			errorMessage = 'Выберите организацию-акцептор';
			return false;
		}
		if (!secondOrgId) {
			errorMessage = 'Выберите организацию-донор';
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
				'/management/absorb',
				{},
				{
					params: {
						absorberId: firstOrgId,
						absorbedId: secondOrgId
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
			<Dialog.Title>Поглощение организации</Dialog.Title>
			<Dialog.Description>
				Провести поглощение одной организации другой, в данном контексте организация-<i>акцептор</i>
				поглощает организацию-<i>донора</i>
			</Dialog.Description>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">
					<span>
						Акцептор<span class="text-red-500">*</span>
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
						Донор<span class="text-red-500">*</span>
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
