<script lang="ts">
	import * as Breadcrumb from '$lib/components/ui/breadcrumb/index.js';
	import { Separator } from '$lib/components/ui/separator/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import * as Table from '$lib/components/ui/table';
	import SearchIcon from '@lucide/svelte/icons/search';
	import * as Tabs from '$lib/components/ui/tabs/index.js';
	import ResetIcon from '@lucide/svelte/icons/rotate-ccw';
	import { Button } from '$lib/components/ui/button';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import { type Organization } from '$lib/types/organization';
	import AbsorbOrganizationDialog from '$lib/components/dialog/absorb-organization.svelte';
	import MergeOrganizationDialog from '$lib/components/dialog/merge-organizations.svelte';
	import Badge from '$lib/components/ui/badge/badge.svelte';
	import ShowCoordinatesDialog from '$lib/components/dialog/show-coordinates.svelte';
	import ShowAddressDialog from '$lib/components/dialog/show-address.svelte';
	import Input from '$lib/components/ui/input/input.svelte';
	import type { AbsorbApi, MergeApi, ShowApi } from '$lib/types/togglers';
	import type { Coordinates } from '$lib/types/coordinates';
	import type { Address } from '$lib/types/address';

	let organizationList = $state<Organization[]>([]);
	let filter = $state<string>('');
	let rating = $state<string>('');
	let tabValue = $state('prefix');
	let inputError = $state(false);

	let toggleShowCoordinatesDialogApi = $state<ShowApi<Coordinates>>({});
	let toggleShowAddressDialogApi = $state<ShowApi<Address>>({});
	let toggleAbsorbOrgDialogApi = $state<AbsorbApi>({});
	let toggleMergeOrgDialogApi = $state<MergeApi>({});

	const orgTypes: Record<string, string> = {
		COMMERCIAL: 'Коммерческая',
		PUBLIC: 'Публичная',
		GOVERNMENT: 'Государственная',
		TRUST: 'Трастовая',
		OPEN_JOINT_STOCK_COMPANY: 'Открытое акционерное общество'
	};

	const formatToRuDate = (isoDate: string, useUTC = false) => {
		const date = new Date(isoDate);

		const twoDigits = (n: number) => n.toString().padStart(2, '0');

		const hours = useUTC ? date.getUTCHours() : date.getHours();
		const minutes = useUTC ? date.getUTCMinutes() : date.getMinutes();
		const day = useUTC ? date.getUTCDate() : date.getDate();
		const month = (useUTC ? date.getUTCMonth() : date.getMonth()) + 1;
		const year = useUTC ? date.getUTCFullYear() : date.getFullYear();

		return `${twoDigits(hours)}:${twoDigits(minutes)} ${twoDigits(day)}.${twoDigits(month)}.${year}`;
	};

	const fetchPrefix = async () => {
		try {
			const resp = await http.get<Organization[]>('/management/search/by-name', {
				params: {
					prefix: filter
				}
			});
			organizationList = resp.data;
		} catch (ex) {
			console.error('Error while fetching organizations by prefix', ex);
			toast('Ошибка при получении организаций');
		}
	};

	const fetchRatingGreater = async (ratingVal: number) => {
		try {
			const resp = await http.get<Organization[]>('/management/search/rating/greater-than', {
				params: {
					minRating: ratingVal
				}
			});
			organizationList = resp.data;
		} catch (ex) {
			console.error('Error while fetching organizations by prefix', ex);
			toast('Ошибка при получении организаций');
		}
	};

	const fetchRatingLower = async (ratingVal: number) => {
		try {
			const resp = await http.get<Organization[]>('/management/search/rating/less-than', {
				params: {
					maxRating: ratingVal
				}
			});
			organizationList = resp.data;
		} catch (ex) {
			console.error('Error while fetching organizations by prefix', ex);
			toast('Ошибка при получении организаций');
		}
	};
</script>

<div class="grid h-full grid-rows-[min-content_min-content_1fr_min-content]">
	<header
		class="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12"
	>
		<div class="flex items-center gap-2 px-4">
			<Sidebar.Trigger class="-ml-1" />
			<Separator orientation="vertical" class="mr-2 data-[orientation=vertical]:h-4" />
			<Breadcrumb.Root>
				<Breadcrumb.List>
					<Breadcrumb.Item class="hidden md:block">
						<Breadcrumb.Link href="/">Главная</Breadcrumb.Link>
					</Breadcrumb.Item>
					<Breadcrumb.Separator class="hidden md:block" />
					<Breadcrumb.Item>
						<Breadcrumb.Page>Операции</Breadcrumb.Page>
					</Breadcrumb.Item>
				</Breadcrumb.List>
			</Breadcrumb.Root>
		</div>
	</header>

	<div class="flex w-full justify-between pr-8 pl-8">
		<div class="flex w-160 gap-2">
			<Tabs.Root bind:value={tabValue}>
				<Tabs.List>
					<Tabs.Trigger value="prefix">Префикс</Tabs.Trigger>
					<Tabs.Trigger value="rating-g">Рейтинг+</Tabs.Trigger>
					<Tabs.Trigger value="rating-l">Рейтинг-</Tabs.Trigger>
				</Tabs.List>
				<Tabs.TabsContent value="prefix"></Tabs.TabsContent>
				<Tabs.TabsContent value="rating-g"></Tabs.TabsContent>
				<Tabs.TabsContent value="rating-l"></Tabs.TabsContent>
			</Tabs.Root>
			{#if tabValue == 'prefix'}
				<Input
					id="prefix"
					placeholder="Поиск по префиксу"
					type="text"
					class={inputError ? 'border-red-500' : ''}
					bind:value={filter}
				/>
				<Button
					variant="outline"
					onclick={() => {
						if (!filter) {
							inputError = true;
							return;
						}
						inputError = false;
						fetchPrefix();
					}}
				>
					<SearchIcon />
				</Button>
			{:else}
				<Input
					id="prefix"
					placeholder="Поиск по рейтингу"
					type="number"
					class={inputError ? 'border-red-500' : ''}
					bind:value={rating}
				/>
				<Button
					variant="outline"
					onclick={() => {
						if (!rating) {
							inputError = true;
							return;
						}
						const ratingVal = Number(rating.toString().replace(',', '.'));
						if (isNaN(ratingVal)) {
							inputError = true;
							return;
						}
						inputError = false;
						if (tabValue === 'rating-g') {
							fetchRatingGreater(ratingVal);
						} else {
							fetchRatingLower(ratingVal);
						}
					}}
				>
					<SearchIcon />
				</Button>
			{/if}
			<Button
				variant="outline"
				onclick={() => {
					filter = '';
					rating = '';
					inputError = false;
					organizationList = [];
				}}
			>
				<ResetIcon />
			</Button>
		</div>
		<div>
			<Button variant="outline" onclick={toggleMergeOrgDialogApi.toggle}>Слияние организаций</Button
			>
			<Button variant="outline" onclick={toggleAbsorbOrgDialogApi.toggle}
				>Поглощение организации</Button
			>
		</div>
	</div>

	<div class="relative flex h-full w-full flex-col justify-between p-8">
		<div class="absolute inset-8 overflow-y-scroll">
			<Table.Root>
				<Table.Header>
					<Table.Row>
						<Table.Head>ID</Table.Head>
						<Table.Head>Имя</Table.Head>
						<Table.Head>Координаты</Table.Head>
						<Table.Head>Дата создания</Table.Head>
						<Table.Head>Официальный адрес</Table.Head>
						<Table.Head>Выручка за год</Table.Head>
						<Table.Head>Сотрудники</Table.Head>
						<Table.Head>Рейтинг</Table.Head>
						<Table.Head>Тип организации</Table.Head>
						<Table.Head>Адрес для корреспонденции</Table.Head>
						<Table.Head class="w-[50px]"></Table.Head>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{#each organizationList as org (org.id)}
						<Table.Row>
							<Table.Cell class="font-medium">{org.id ?? '-'}</Table.Cell>
							<Table.Cell>{org.name ?? '-'}</Table.Cell>
							<Table.Cell>
								{#if org.coordinatesId}
									<Badge
										class="cursor-pointer"
										variant="secondary"
										onclick={() => {
											if (toggleShowCoordinatesDialogApi.toggleById && org.coordinatesId) {
												toggleShowCoordinatesDialogApi.toggleById(org.coordinatesId);
											}
										}}>{'Координаты #' + org.coordinatesId}</Badge
									>
								{:else}
									-
								{/if}
							</Table.Cell>
							<Table.Cell>{formatToRuDate(org.creationDate) ?? '-'}</Table.Cell>
							<Table.Cell>
								{#if org.officialAddressId}
									<Badge
										class="cursor-pointer"
										variant="secondary"
										onclick={() => {
											if (toggleShowAddressDialogApi.toggleById && org.officialAddressId) {
												toggleShowAddressDialogApi.toggleById(org.officialAddressId);
											}
										}}>{'Адрес #' + org.officialAddressId}</Badge
									>
								{:else}
									-
								{/if}
							</Table.Cell>
							<Table.Cell>{org.annualTurnover ?? '-'}</Table.Cell>
							<Table.Cell>{org.employeesCount ?? '-'}</Table.Cell>
							<Table.Cell>{org.rating ?? '-'}</Table.Cell>
							<Table.Cell>{org.type ? orgTypes[org.type] : '-'}</Table.Cell>
							<Table.Cell>
								{#if org.postalAddressId}
									<Badge
										class="cursor-pointer"
										variant="secondary"
										onclick={() => {
											if (toggleShowAddressDialogApi.toggleById && org.postalAddressId) {
												toggleShowAddressDialogApi.toggleById(org.postalAddressId);
											}
										}}>{'Адрес #' + org.postalAddressId}</Badge
									>
								{:else}
									-
								{/if}
							</Table.Cell>
						</Table.Row>
					{/each}
				</Table.Body>
			</Table.Root>
		</div>
	</div>
</div>

<ShowCoordinatesDialog bind:expose={toggleShowCoordinatesDialogApi} />
<ShowAddressDialog bind:expose={toggleShowAddressDialogApi} />
<AbsorbOrganizationDialog bind:expose={toggleAbsorbOrgDialogApi} />
<MergeOrganizationDialog bind:expose={toggleMergeOrgDialogApi} />
