<script lang="ts">
	import * as Breadcrumb from '$lib/components/ui/breadcrumb/index.js';
	import { Separator } from '$lib/components/ui/separator/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import * as Table from '$lib/components/ui/table';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
	import SearchIcon from '@lucide/svelte/icons/search';
	import * as Tabs from '$lib/components/ui/tabs/index.js';
	import ResetIcon from '@lucide/svelte/icons/rotate-ccw';
	import { Label } from '$lib/components/ui/label';
	import * as Select from '$lib/components/ui/select';
	import { Button } from '$lib/components/ui/button';
	import ChevronsLeftIcon from '@tabler/icons-svelte/icons/chevrons-left';
	import ChevronLeftIcon from '@tabler/icons-svelte/icons/chevron-left';
	import ChevronRightIcon from '@tabler/icons-svelte/icons/chevron-right';
	import ChevronsRightIcon from '@tabler/icons-svelte/icons/chevrons-right';
	import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
	import DotsVerticalIcon from '@tabler/icons-svelte/icons/dots-vertical';
	import ws from '$lib/api/ws';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import { onDestroy, onMount } from 'svelte';
	import type { Data } from '$lib/types/data';
	import { type Organization } from '$lib/types/organization';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import EditOrganizationDialog from '$lib/components/dialog/edit-organization.svelte';
	import CreateOrganizationDialog from '$lib/components/dialog/create-organization.svelte';
	import AbsorbOrganizationDialog from '$lib/components/dialog/absorb-organization.svelte';
	import MergeOrganizationDialog from '$lib/components/dialog/merge-organizations.svelte';
	import Badge from '$lib/components/ui/badge/badge.svelte';
	import ShowAdressDialog from '$lib/components/dialog/show-organization.svelte';
	import ShowCoordinatesDialog from '$lib/components/dialog/show-coordinates.svelte';
	import ShowAddressDialog from '$lib/components/dialog/show-address.svelte';
	import ShowLocationDialog from '$lib/components/dialog/show-location.svelte';
	import Input from '$lib/components/ui/input/input.svelte';

	let organizationList = $state<Organization[]>([]);
	let filter = $state<string>('');
	let rating = $state<string>('');
	let currentFilter = $state<string | null>(null);
	let tabValue = $state('prefix');
	let inputError = $state(false);

	let toggleEditDialogApi = $state<any>({});
	let toggleCreateDialogApi = $state<any>({});
	let toggleShowOrganizationDialogApi = $state<any>({});
	let toggleShowCoordinatesDialogApi = $state<any>({});
	let toggleShowLocationDialogApi = $state<any>({});
	let toggleShowAddressDialogApi = $state<any>({});
	let toggleAbsorbOrgDialogApi = $state<any>({});
	let toggleMergeOrgDialogApi = $state<any>({});

	const orgTypes: Record<string, string> = {
		COMMERCIAL: 'Коммерческая',
		PUBLIC: 'Публичная',
		GOVERNMENT: 'Государственная',
		TRUST: 'Трастовая',
		OPEN_JOINT_STOCK_COMPANY: 'Открытое акционерное общество'
	};

	onMount(() => {});

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
			console.error('Error while fetching organizations by prefix');
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
			console.error('Error while fetching organizations by prefix');
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
			console.error('Error while fetching organizations by prefix');
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
										onclick={() => toggleShowCoordinatesDialogApi.toggleById(org.coordinatesId)}
										>{'Координаты #' + org.coordinatesId}</Badge
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
										onclick={() => toggleShowAddressDialogApi.toggleById(org.officialAddressId)}
										>{'Адрес #' + org.officialAddressId}</Badge
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
										onclick={() => toggleShowAddressDialogApi.toggleById(org.postalAddressId)}
										>{'Адрес #' + org.postalAddressId}</Badge
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

<ShowAdressDialog bind:expose={toggleShowOrganizationDialogApi} />
<EditOrganizationDialog bind:expose={toggleEditDialogApi} />
<CreateOrganizationDialog bind:expose={toggleCreateDialogApi} />
<ShowCoordinatesDialog bind:expose={toggleShowCoordinatesDialogApi} />
<ShowAddressDialog bind:expose={toggleShowAddressDialogApi} />
<AbsorbOrganizationDialog bind:expose={toggleAbsorbOrgDialogApi} />
<MergeOrganizationDialog bind:expose={toggleMergeOrgDialogApi} />
