<script lang="ts">
	import * as Breadcrumb from '$lib/components/ui/breadcrumb/index.js';
	import { Separator } from '$lib/components/ui/separator/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import * as Table from '$lib/components/ui/table';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
	import SearchIcon from '@lucide/svelte/icons/search';
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
	import Badge from '$lib/components/ui/badge/badge.svelte';
	import ShowAdressDialog from '$lib/components/dialog/show-organization.svelte';
	import ShowCoordinatesDialog from '$lib/components/dialog/show-coordinates.svelte';
	import ShowAddressDialog from '$lib/components/dialog/show-address.svelte';
	import ShowLocationDialog from '$lib/components/dialog/show-location.svelte';
	import Input from '$lib/components/ui/input/input.svelte';

	let currentPage = $state(0);
	let totalElements = $state(0);
	let pageSize = $state(10);
	let pageSizeVariants = [10, 20, 30, 40, 50];
	let organizationList = $state<Organization[]>([]);
	let filter = $state<string>('');
	let currentFilter = $state<string | null>(null);

	let toggleEditDialogApi = $state<any>({});
	let toggleCreateDialogApi = $state<any>({});
	let toggleShowOrganizationDialogApi = $state<any>({});
	let toggleShowCoordinatesDialogApi = $state<any>({});
	let toggleShowLocationDialogApi = $state<any>({});
	let toggleShowAddressDialogApi = $state<any>({});

	const orgTypes: Record<string, string> = {
		COMMERCIAL: 'Коммерческая',
		PUBLIC: 'Публичная',
		GOVERNMENT: 'Государственная',
		TRUST: 'Трастовая',
		OPEN_JOINT_STOCK_COMPANY: 'Открытое акционерное общество'
	};

	const totalPages = $derived(Math.max(Math.ceil(totalElements / pageSize), 1));
	const prevPageAvailable = $derived(currentPage > 0);
	const nextPageAvailable = $derived(currentPage + 1 < totalPages);

	let unsubscribe: (() => void) | null = null;
	type columns = 'annualTurnover' | 'employeesCount' | 'rating';
	let sortColumn = $state<columns | null>(null);
	let sortDirection = $state<'asc' | 'desc' | null>(null);

	const updateSort = (col: columns) => {
		if (sortColumn === null || sortColumn !== col) {
			sortColumn = col;
			sortDirection = 'asc';
		} else if (sortColumn === col && sortDirection == 'asc') {
			sortDirection = 'desc';
		} else {
			sortColumn = null;
			sortDirection = null;
		}
		fetchData();
	};

	const showSortDirection = (col: columns) => {
		if (col != sortColumn) {
			return '';
		}
		if (sortDirection == 'asc') {
			return '↓';
		}
		return '↑';
	};

	onMount(() => {
		fetchData();
		unsubscribe = ws.subscribe(() => {
			fetchData();
		});
	});

	onDestroy(() => {
		if (unsubscribe) {
			unsubscribe();
		}
	});

	const fetchData = async () => {
		try {
			const resp = await http.get<Data<number>>('/management/organization/total');
			totalElements = resp.data.data;
			const respOrganization = await http.get<Organization[]>('/management/organization', {
				params: {
					page: currentPage,
					size: pageSize,
					filter: currentFilter,
					sortBy: sortColumn ?? 'id',
					sort: sortDirection ?? 'asc'
				}
			});
			organizationList = respOrganization.data;
			// toast('Данные загружены');
		} catch (ex) {
			console.error('Error while fetching organization', ex);
			toast('Ошибка при загрузке данных');
		}
	};

	const gotoFirstPage = () => {
		currentPage = 0;
		fetchData();
	};

	const gotoPrevPage = () => {
		currentPage--;
		fetchData();
	};

	const gotoNextPage = () => {
		currentPage++;
		fetchData();
	};

	const gotoLastPage = () => {
		currentPage = totalPages - 1;
		fetchData();
	};

	const updatePageSize = (size: string) => {
		pageSize = Number(size);
		fetchData();
	};

	$effect(() => {
		if (currentPage > totalPages - 1 && currentPage > 0) {
			currentPage = totalPages - 1;
			fetchData();
		}
	});

	const duplicateItem = async (item: Organization) => {
		try {
			await http.post(`/management/organization`, {
				id: item?.id,
				ownerId: item?.ownerId,
				name: item?.name,
				coordinatesId: item?.coordinatesId,
				creationDate: item?.creationDate,
				officialAddressId: item?.officialAddressId,
				annualTurnover: item?.annualTurnover,
				employeesCount: item?.employeesCount,
				rating: item?.rating,
				type: item?.type,
				postalAddressId: item?.postalAddressId
			});
		} catch (ex) {
			toast('Ошибка при дублировании элемента');
			console.error('Error while deleting item', ex);
		}
	};

	const deleteItem = async (item: Organization) => {
		try {
			await http.delete(`/management/organization/${item.id}`);
		} catch (ex) {
			toast('Ошибка при удалении элемента');
			console.error('Error while deleting item', ex);
		}
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
					<Breadcrumb.Item class="hidden md:block">
						<Breadcrumb.Link href="/management">Предметная область</Breadcrumb.Link>
					</Breadcrumb.Item>
					<Breadcrumb.Separator class="hidden md:block" />
					<Breadcrumb.Item>
						<Breadcrumb.Page>Организации</Breadcrumb.Page>
					</Breadcrumb.Item>
				</Breadcrumb.List>
			</Breadcrumb.Root>
		</div>
	</header>

	<div class="flex w-125 gap-2 px-8">
		<Input id="zipCode" placeholder="Поиск по названию" type="text" bind:value={filter} />
		<Button
			variant="outline"
			onclick={() => {
				if (filter) {
					currentFilter = filter;
				} else {
					currentFilter = null;
				}
				fetchData();
			}}
		>
			<SearchIcon />
		</Button>
		<Button
			variant="outline"
			onclick={() => {
				filter = '';
				currentFilter = null;
				fetchData();
			}}
		>
			<ResetIcon />
		</Button>
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
						<Table.Head class="cursor-pointer" onclick={() => updateSort('annualTurnover')}
							>Выручка за год{showSortDirection('annualTurnover')}</Table.Head
						>
						<Table.Head class="cursor-pointer" onclick={() => updateSort('employeesCount')}
							>Сотрудники{showSortDirection('employeesCount')}</Table.Head
						>
						<Table.Head class="cursor-pointer" onclick={() => updateSort('rating')}
							>Рейтинг{showSortDirection('rating')}</Table.Head
						>
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
							<Table.Cell>
								{@render DataTableActions(org)}
							</Table.Cell>
						</Table.Row>
					{/each}
				</Table.Body>
			</Table.Root>
		</div>
	</div>
	<div class="mt-8 mb-4 flex items-center justify-between gap-4 px-4">
		<Button onclick={() => toggleCreateDialogApi.toggle()} variant="outline" size="sm">
			<PlusIcon />
			<span class="hidden lg:inline">Добавить новый элемент</span>
		</Button>
		<div class="flex w-full items-center gap-8 lg:w-fit">
			<div class="hidden items-center gap-2 lg:flex">
				<Label for="rows-per-page" class="text-sm font-medium">Строк на странице</Label>
				<Select.Root type="single" bind:value={() => pageSize.toString(), updatePageSize}>
					<Select.Trigger size="sm" class="w-20" id="rows-per-page">
						{pageSize}
					</Select.Trigger>
					<Select.Content side="top">
						{#each pageSizeVariants as pageSizeVariant (pageSizeVariant)}
							<Select.Item value={pageSizeVariant.toString()}>
								{pageSizeVariant}
							</Select.Item>
						{/each}
					</Select.Content>
				</Select.Root>
			</div>
			<div class="flex w-fit items-center justify-center text-sm font-medium">
				Страница {currentPage + 1}
				из {totalPages}
			</div>
			<div class="ml-auto flex items-center gap-2 lg:ml-0">
				<Button
					variant="outline"
					class="hidden h-8 w-8 p-0 lg:flex"
					onclick={gotoFirstPage}
					disabled={!prevPageAvailable}
				>
					<span class="sr-only">Перейти на первую страницу</span>
					<ChevronsLeftIcon />
				</Button>
				<Button
					variant="outline"
					class="size-8"
					size="icon"
					onclick={gotoPrevPage}
					disabled={!prevPageAvailable}
				>
					<span class="sr-only">Перейти на предыдущую страницу</span>
					<ChevronLeftIcon />
				</Button>
				<Button
					variant="outline"
					class="size-8"
					size="icon"
					onclick={gotoNextPage}
					disabled={!nextPageAvailable}
				>
					<span class="sr-only">Перейти на следующую страницу</span>
					<ChevronRightIcon />
				</Button>
				<Button
					variant="outline"
					class="hidden size-8 lg:flex"
					size="icon"
					onclick={gotoLastPage}
					disabled={!nextPageAvailable}
				>
					<span class="sr-only">Перейти на последнюю страницу</span>
					<ChevronsRightIcon />
				</Button>
			</div>
		</div>
	</div>
</div>

<ShowAdressDialog bind:expose={toggleShowOrganizationDialogApi} />
<EditOrganizationDialog bind:expose={toggleEditDialogApi} />
<CreateOrganizationDialog bind:expose={toggleCreateDialogApi} />
<ShowCoordinatesDialog bind:expose={toggleShowCoordinatesDialogApi} />
<ShowAddressDialog bind:expose={toggleShowAddressDialogApi} />

{#snippet DataTableActions(item: Organization)}
	<DropdownMenu.Root>
		<DropdownMenu.Trigger class="flex size-8 text-muted-foreground data-[state=open]:bg-muted">
			{#snippet child({ props })}
				<Button variant="ghost" size="icon" {...props}>
					<DotsVerticalIcon />
					<span class="sr-only">Открыть меню</span>
				</Button>
			{/snippet}
		</DropdownMenu.Trigger>
		<DropdownMenu.Content align="end" class="w-32">
			<DropdownMenu.Item onclick={() => toggleShowOrganizationDialogApi.toggle(item)}
				>Информация</DropdownMenu.Item
			>
			<DropdownMenu.Item onclick={() => toggleEditDialogApi.toggle(item)}
				>Изменить</DropdownMenu.Item
			>
			<DropdownMenu.Item onclick={() => duplicateItem(item)}>Дублировать</DropdownMenu.Item>
			<DropdownMenu.Separator />
			<DropdownMenu.Item variant="destructive" onclick={() => deleteItem(item)}>
				Удалить
			</DropdownMenu.Item>
		</DropdownMenu.Content>
	</DropdownMenu.Root>
{/snippet}
