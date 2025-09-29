<script lang="ts">
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { Organization } from '$lib/types/organization';
	import Button from '../ui/button/button.svelte';
	import Input from '../ui/input/input.svelte';
	import Label from '../ui/label/label.svelte';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import ShowCoordinatesDialog from '$lib/components/dialog/show-coordinates.svelte';
	import ShowAddressDialog from '$lib/components/dialog/show-address.svelte';
	import * as Select from '$lib/components/ui/select';
	import type { Coordinates } from '$lib/types/coordinates';
	import type { ShowApi } from '$lib/types/togglers';
	import type { Address } from '$lib/types/address';

	let open = $state(false);
	let { expose = $bindable({}) } = $props();

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

	let showAddressApi = $state<ShowApi<Address>>({});
	let showCoordinatesApi = $state<ShowApi<Coordinates>>({});

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

	expose.toggleById = async (orgId: number) => {
		try {
			const resp = await http.get<Organization>(`/management/organization/${orgId}`);
			const org = resp.data;
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
		} catch (ex) {
			toast('Ошибка загрузки организации');
			console.error('Error while fetching organization', ex);
		}
	};

	const orgTypes: Record<string, string> = {
		NULL: '-',
		COMMERCIAL: 'Коммерческая',
		PUBLIC: 'Публичная',
		GOVERNMENT: 'Государственная',
		TRUST: 'Трастовая',
		OPEN_JOINT_STOCK_COMPANY: 'Открытое акционерное общество'
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
			<Dialog.Title>Просмотр организации</Dialog.Title>
			<Dialog.Description>
				Отобразить текущую организацию и её состояние в хранилище
			</Dialog.Description>
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
				<Label for="name" class="text-right">Имя</Label>
				<Input id="name" bind:value={name} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Координаты</Label>
				<Button
					id="town"
					class="col-span-3"
					variant="outline"
					onclick={() => {
						if (showCoordinatesApi.toggleById && coordId) {
							showCoordinatesApi.toggleById(coordId);
						}
					}}
					disabled={!coordId}
				>
					{#if coordId}
						Координаты #{coordId}
					{:else}
						Не выбрано
					{/if}
				</Button>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="name" class="text-right">Создана</Label>
				<Input id="name" value={formatToRuDate(creationDate)} disabled class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Оф. адрес</Label>
				<Button
					id="town"
					class="col-span-3"
					variant="outline"
					onclick={() => {
						if (showAddressApi.toggleById && officialAddressId) {
							showAddressApi.toggleById(officialAddressId);
						}
					}}
					disabled={!officialAddressId}
				>
					{#if officialAddressId}
						Адрес #{officialAddressId}
					{:else}
						Не выбрано
					{/if}
				</Button>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="annualTurnover" class="text-right">Выр. за год</Label>
				<Input
					id="annualTurnover"
					type="number"
					disabled
					bind:value={annualTurnover}
					class="col-span-3"
				/>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="employeesCount" class="text-right">Сотрудники</Label>
				<Input
					id="employeesCount"
					type="number"
					disabled
					bind:value={employeesCount}
					class="col-span-3"
				/>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="rating" class="text-right">Рейтинг</Label>
				<Input id="rating" type="number" disabled bind:value={rating} class="col-span-3" />
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="type" class="text-right">Тип</Label>
				<div class="col-span-3">
					<Select.Root type="single" disabled bind:value={organizationType}>
						<Select.Trigger class="w-full" id="rows-per-page">
							{orgTypes[organizationType] ?? '-'}
						</Select.Trigger>
						<Select.Content side="top">
							<Select.Item value="NULL">-</Select.Item>
							<Select.Item value="COMMERCIAL">Коммерческая</Select.Item>
							<Select.Item value="PUBLIC">Публичная</Select.Item>
							<Select.Item value="GOVERNMENT">Государственная</Select.Item>
							<Select.Item value="TRUST">Трастовая</Select.Item>
							<Select.Item value="OPEN_JOINT_STOCK_COMPANY"
								>Открытое акционерное общество</Select.Item
							>
						</Select.Content>
					</Select.Root>
				</div>
			</div>
			<div class="grid grid-cols-4 items-center gap-4">
				<Label for="town" class="text-right">Почт. адрес</Label>
				<Button
					id="town"
					class="col-span-3"
					variant="outline"
					onclick={() => {
						if (showAddressApi.toggleById && postalAddressId) {
							showAddressApi.toggleById(postalAddressId);
						}
					}}
					disabled={!postalAddressId}
				>
					{#if postalAddressId}
						Адрес #{postalAddressId}
					{:else}
						Не выбрано
					{/if}
				</Button>
			</div>
		</div>
	</Dialog.Content>
</Dialog.Root>

<ShowCoordinatesDialog bind:expose={showCoordinatesApi} />
<ShowAddressDialog bind:expose={showAddressApi} />
