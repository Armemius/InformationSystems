<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Organization } from '$lib/types/organization';
	import type { Location } from '$lib/types/location';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
	import SelectCoordinatesDialog from '$lib/components/dialog/select-coordinates.svelte';
	import CreateCoordinatesDialog from '$lib/components/dialog/create-coordinates.svelte';
	import SelectAddressDialog from '$lib/components/dialog/select-address.svelte';
	import CreateAddressDialog from '$lib/components/dialog/create-address.svelte';
	import { type Coordinates } from '$lib/types/coordinates';
	import { type Address } from '$lib/types/address';
	import * as Select from '$lib/components/ui/select';

	let open = $state(false);
	let { expose = $bindable({}), callback = (addr: Organization) => {} } = $props();

	let id = $state<number | null>(null);
	let ownerId = $state<number | null>(null);
	let creationDate = $state<string>('');
	let name = $state('');
	let coordId = $state<number | null>(null);
	let officialAddressId = $state<number | null>(null);
	let annualTurnover = $state('');
	let employeesCount = $state('');
	let rating = $state('');
	let organizationType = $state('NULL');
	let postalAddressId = $state<number | null>(null);
	let errorMessage = $state('');

	let selectCoordinatesApi = $state<any>({});
	let selectOfficialAddressApi = $state<any>({});
	let selectPostalAddressApi = $state<any>({});
	let createCoordinatesApi = $state<any>({});
	let createOfficialAddressApi = $state<any>({});
	let createPostalAddressApi = $state<any>({});

	expose.toggle = (org: Organization) => {
		id = org.id ?? null;
		ownerId = org.ownerId ?? null;
		name = org.name;
		coordId = org.coordinatesId;
		creationDate = org.creationDate;
		officialAddressId = org.officialAddressId ?? null;
		annualTurnover = String(org.annualTurnover);
		employeesCount = String(org.employeesCount);
		rating = String(org.rating);
		organizationType = org.type ?? 'NULL';
		postalAddressId = org.postalAddressId ?? null;
		open = true;
	};

	const orgTypes: Record<string, string> = {
		NULL: '-',
		COMMERCIAL: 'Коммерческая',
		PUBLIC: 'Публичная',
		GOVERNMENT: 'Государственная',
		TRUST: 'Трастовая',
		OPEN_JOINT_STOCK_COMPANY: 'Открытое акционерное общество'
	};

	const validate = () => {
		if (!name) {
			errorMessage = 'Заполните имя';
			return false;
		}
		if (!coordId) {
			errorMessage = 'Выберите координаты';
			return false;
		}
		if (!annualTurnover) {
			errorMessage = 'Заполните выручку за год';
			return false;
		}
		if (!employeesCount) {
			errorMessage = 'Заполните кол-во сотрудников';
			return false;
		}
		if (!rating) {
			errorMessage = 'Заполните рейтинг';
			return false;
		}
		const annualTurnoverValue = Number(annualTurnover.toString().replace(',', '.'));
		if (isNaN(annualTurnoverValue)) {
			errorMessage = 'Выручка за год должна быть числом';
			return false;
		}
		if (annualTurnoverValue < 1) {
			errorMessage = 'Выручка за год должна быть больше 1';
			return false;
		}
		const employeesCountValue = Number(employeesCount.toString().replace(',', '.'));
		if (isNaN(employeesCountValue)) {
			errorMessage = 'Кол-во сотрудников должно быть числом';
			return false;
		}
		if (employeesCountValue < 1) {
			errorMessage = 'Кол-во сотрудников должно быть больше 1';
			return false;
		}
		const ratingValue = Number(rating.toString().replace(',', '.'));
		if (isNaN(ratingValue)) {
			errorMessage = 'Рейтинг должен быть числом';
			return false;
		}
		if (ratingValue < 1) {
			errorMessage = 'Рейтинг должен быть больше 1';
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
			const resp = await http.patch<Organization>(`/management/organization/${id}`, {
				name: name,
				coordinatesId: coordId,
				officialAddressId: officialAddressId,
				annualTurnover: annualTurnover.toString().replace(',', '.'),
				employeesCount: employeesCount.toString().replace(',', '.'),
				rating: rating.toString().replace(',', '.'),
				type: organizationType ? (organizationType === 'NULL' ? null : organizationType) : null,
				postalAddressId: postalAddressId
			});
			const addr = resp.data;
			callback(addr);
			open = false;
		} catch (ex) {
			console.error('Error while adding organization', ex);
			toast('Ошибка при добавлении организации');
		}
	};

	const formatToRuDate = (isoDate: string, useUTC = false) => {
		if (!isoDate) {
			return '-';
		}
		const date = new Date(isoDate);

		const twoDigits = (n: number) => n.toString().padStart(2, '0');

		const hours = useUTC ? date.getUTCHours() : date.getHours();
		const minutes = useUTC ? date.getUTCMinutes() : date.getMinutes();
		const day = useUTC ? date.getUTCDate() : date.getDate();
		const month = (useUTC ? date.getUTCMonth() : date.getMonth()) + 1;
		const year = useUTC ? date.getUTCFullYear() : date.getFullYear();

		return `${twoDigits(hours)}:${twoDigits(minutes)} ${twoDigits(day)}.${twoDigits(month)}.${year}`;
	};
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Обновление организации</Dialog.Title>
			<Dialog.Description
				>Изменить существующую организацию и обновить её состояние в хранилище</Dialog.Description
			>
		</Dialog.Header>
		<div class="grid gap-4 py-4">
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">ID</Label>
				<Input id="name" value={id ?? '-'} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">ID владельца</Label>
				<Input id="name" value={ownerId ?? '-'} disabled class="col-span-3" />
			</div>
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
						Координаты<span class="text-red-500">*</span>
					</span>
				</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-4"
						variant="secondary"
						onclick={selectCoordinatesApi.toggle}
					>
						{#if coordId}
							Координаты #{coordId}
						{:else}
							Выбрать
						{/if}
					</Button>
					<Button
						id="town"
						class="col-span-1"
						variant="outline"
						onclick={createCoordinatesApi.toggle}
					>
						<PlusIcon />
					</Button>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">Создана</Label>
				<Input id="name" value={formatToRuDate(creationDate)} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Оф. адрес</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-4"
						variant="secondary"
						onclick={selectOfficialAddressApi.toggle}
					>
						{#if officialAddressId}
							Адрес #{officialAddressId}
						{:else}
							Выбрать
						{/if}
					</Button>
					<Button
						id="town"
						class="col-span-1"
						variant="outline"
						onclick={createOfficialAddressApi.toggle}
					>
						<PlusIcon />
					</Button>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="annualTurnover" class="text-right">
					<span>
						Выр. за год<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="annualTurnover" type="number" bind:value={annualTurnover} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="employeesCount" class="text-right">
					<span>
						Сотрудники<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="employeesCount" type="number" bind:value={employeesCount} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="rating" class="text-right">
					<span>
						Рейтинг<span class="text-red-500">*</span>
					</span>
				</Label>
				<Input id="rating" type="number" bind:value={rating} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="type" class="text-right">Тип</Label>
				<div class="col-span-3">
					<Select.Root type="single" bind:value={organizationType}>
						<Select.Trigger class="w-full" id="rows-per-page">
							{orgTypes[organizationType] ?? '-'}
						</Select.Trigger>
						<Select.Content side="top">
							<Select.Item value={'NULL'}>-</Select.Item>
							<Select.Item value={'COMMERCIAL'}>Коммерческая</Select.Item>
							<Select.Item value={'PUBLIC'}>Публичная</Select.Item>
							<Select.Item value={'GOVERNMENT'}>Государственная</Select.Item>
							<Select.Item value={'TRUST'}>Трастовая</Select.Item>
							<Select.Item value={'OPEN_JOINT_STOCK_COMPANY'}
								>Открытое акционерное общество</Select.Item
							>
						</Select.Content>
					</Select.Root>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Почт. адрес</Label>
				<div class="col-span-3 grid grid-cols-5 gap-2">
					<Button
						id="town"
						class="col-span-4"
						variant="secondary"
						onclick={selectPostalAddressApi.toggle}
					>
						{#if postalAddressId}
							Адрес #{postalAddressId}
						{:else}
							Выбрать
						{/if}
					</Button>
					<Button
						id="town"
						class="col-span-1"
						variant="outline"
						onclick={createPostalAddressApi.toggle}
					>
						<PlusIcon />
					</Button>
				</div>
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

<SelectCoordinatesDialog
	bind:expose={selectCoordinatesApi}
	callback={(coords: Coordinates) => {
		coordId = coords?.id ?? null;
	}}
/>
<CreateCoordinatesDialog
	bind:expose={createCoordinatesApi}
	callback={(coords: Coordinates) => {
		coordId = coords?.id ?? null;
	}}
/>
<SelectAddressDialog
	bind:expose={selectOfficialAddressApi}
	callback={(addr: Address) => {
		officialAddressId = addr?.id ?? null;
	}}
/>
<CreateAddressDialog
	bind:expose={createOfficialAddressApi}
	callback={(addr: Address) => {
		officialAddressId = addr?.id ?? null;
	}}
/>
<SelectAddressDialog
	bind:expose={selectPostalAddressApi}
	callback={(addr: Address) => {
		postalAddressId = addr?.id ?? null;
	}}
/>
<CreateAddressDialog
	bind:expose={createPostalAddressApi}
	callback={(addr: Address) => {
		postalAddressId = addr?.id ?? null;
	}}
/>
