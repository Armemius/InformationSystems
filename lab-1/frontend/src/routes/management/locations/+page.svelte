<script lang="ts">
	import * as Breadcrumb from '$lib/components/ui/breadcrumb/index.js';
	import { Separator } from '$lib/components/ui/separator/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import * as Table from '$lib/components/ui/table';
	import PlusIcon from '@tabler/icons-svelte/icons/plus';
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
	import { type Location } from '$lib/types/location';
	import EditLocationDialog from '$lib/components/dialog/edit-location.svelte';
	import CreateLocationDialog from '$lib/components/dialog/create-location.svelte';
	import ShowLocationDialog from '$lib/components/dialog/show-location.svelte';
	import type { CreateApi, EditApi, ShowApi } from '$lib/types/togglers';

	let currentPage = $state(0);
	let totalElements = $state(0);
	let pageSize = $state(10);
	let pageSizeVariants = [10, 20, 30, 40, 50];
	let locationsList = $state<Location[]>([]);

	let toggleShowDialogApi = $state<ShowApi<Location>>({});
	let toggleEditDialogApi = $state<EditApi<Location>>({});
	let toggleCreateDialogApi = $state<CreateApi>({});

	const totalPages = $derived(Math.max(Math.ceil(totalElements / pageSize), 1));
	const prevPageAvailable = $derived(currentPage > 0);
	const nextPageAvailable = $derived(currentPage + 1 < totalPages);

	let unsubscribe: (() => void) | null = null;
	type columns = 'x' | 'y' | 'z';
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
			const resp = await http.get<Data<number>>('/management/location/total');
			totalElements = resp.data.data;
			const respLocation = await http.get<Location[]>('/management/location', {
				params: {
					page: currentPage,
					size: pageSize,
					sortBy: sortColumn ?? 'id',
					sort: sortDirection ?? 'asc'
				}
			});
			locationsList = respLocation.data;
			// toast('Данные загружены');
		} catch (ex) {
			console.error('Error while fetching locations', ex);
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

	const duplicateItem = async (item: Location) => {
		try {
			await http.post(`/management/location`, {
				x: item.x,
				y: item.y,
				z: item.z
			});
		} catch (ex) {
			toast('Ошибка при дублировании элемента');
			console.error('Error while deleting item', ex);
		}
	};

	const deleteItem = async (item: Location) => {
		try {
			await http.delete(`/management/location/${item.id}`);
		} catch (ex) {
			toast('Ошибка при удалении элемента');
			console.error('Error while deleting item', ex);
		}
	};
</script>

<div class="grid h-full grid-rows-[min-content_1fr_min-content]">
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
						<Breadcrumb.Page>Локации</Breadcrumb.Page>
					</Breadcrumb.Item>
				</Breadcrumb.List>
			</Breadcrumb.Root>
		</div>
	</header>

	<div class="relative flex h-full w-full flex-col justify-between p-8">
		<div class="absolute inset-8 overflow-y-scroll">
			<Table.Root>
				<Table.Header>
					<Table.Row>
						<Table.Head class="w-[100px]">ID</Table.Head>
						<Table.Head class="cursor-pointer" onclick={() => updateSort('x')}
							>X{showSortDirection('x')}</Table.Head
						>
						<Table.Head class="cursor-pointer" onclick={() => updateSort('y')}
							>Y{showSortDirection('y')}</Table.Head
						>
						<Table.Head class="cursor-pointer" onclick={() => updateSort('z')}
							>Z{showSortDirection('z')}</Table.Head
						>
						<Table.Head class="w-[50px]"></Table.Head>
					</Table.Row>
				</Table.Header>
				<Table.Body>
					{#each locationsList as location (location.id)}
						<Table.Row>
							<Table.Cell class="font-medium">{location.id ?? 'N/A'}</Table.Cell>
							<Table.Cell>{location.x ?? 'N/A'}</Table.Cell>
							<Table.Cell>{location.y ?? 'N/A'}</Table.Cell>
							<Table.Cell>{location.z ?? 'N/A'}</Table.Cell>
							<Table.Cell>
								{@render DataTableActions(location)}
							</Table.Cell>
						</Table.Row>
					{/each}
				</Table.Body>
			</Table.Root>
		</div>
	</div>
	<div class="mt-8 mb-4 flex items-center justify-between gap-4 px-4">
		<Button
			onclick={() => {
				if (toggleCreateDialogApi.toggle) {
					toggleCreateDialogApi.toggle();
				}
			}}
			variant="outline"
			size="sm"
		>
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

<EditLocationDialog bind:expose={toggleEditDialogApi} />
<CreateLocationDialog bind:expose={toggleCreateDialogApi} />
<ShowLocationDialog bind:expose={toggleShowDialogApi} />

{#snippet DataTableActions(item: Location)}
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
			<DropdownMenu.Item
				onclick={() => {
					if (toggleShowDialogApi.toggle) {
						toggleShowDialogApi.toggle(item);
					}
				}}>Информация</DropdownMenu.Item
			>
			<DropdownMenu.Item
				onclick={() => {
					if (toggleEditDialogApi.toggle) {
						toggleEditDialogApi.toggle(item);
					}
				}}>Изменить</DropdownMenu.Item
			>
			<DropdownMenu.Item onclick={() => duplicateItem(item)}>Дублировать</DropdownMenu.Item>
			<DropdownMenu.Separator />
			<DropdownMenu.Item variant="destructive" onclick={() => deleteItem(item)}>
				Удалить
			</DropdownMenu.Item>
		</DropdownMenu.Content>
	</DropdownMenu.Root>
{/snippet}
