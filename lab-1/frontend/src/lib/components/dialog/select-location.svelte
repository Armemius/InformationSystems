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
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import EditLocationDialog from '$lib/components/dialog/edit-location.svelte';
	import CreateLocationDialog from '$lib/components/dialog/create-location.svelte';
	import ShowLocationDialog from '$lib/components/dialog/show-location.svelte';

	let open = $state(false);
	let { expose = $bindable({}), callback = (loc: Location) => {} } = $props();

	expose.toggle = () => {
		currentLocation = null;
		currentPage = 0;
		totalElements = 0;
		open = true;
		fetchData();
	};

	let currentLocation = $state<Location | null>(null);

	let currentPage = $state(0);
	let totalElements = $state(0);
	let pageSize = $state(10);
	let locationList = $state<Location[]>([]);
	const totalPages = $derived(Math.max(Math.ceil(totalElements / pageSize), 1));
	const prevPageAvailable = $derived(currentPage > 0);
	const nextPageAvailable = $derived(currentPage + 1 < totalPages);

	const submit = async () => {
		if (currentLocation) {
			callback(currentLocation);
			open = false;
		}
	};

	let unsubscribe: (() => void) | null = null;

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
					size: pageSize
				}
			});
			locationList = respLocation.data;
		} catch (ex) {
			console.error('Error while fetching location', ex);
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
</script>

<Dialog.Root bind:open>
	<Dialog.Content>
		<Dialog.Header>
			<Dialog.Title>Выбор локации</Dialog.Title>
			<Dialog.Description
				>Меню для выбора существующей локации для последующего использования в других объектах</Dialog.Description
			>
		</Dialog.Header>
		<Table.Root>
			<Table.Header>
				<Table.Row>
					<Table.Head class="w-[100px]">ID</Table.Head>
					<Table.Head>X</Table.Head>
					<Table.Head>Y</Table.Head>
					<Table.Head>Z</Table.Head>
				</Table.Row>
			</Table.Header>
			<Table.Body>
				{#each locationList as loc (loc.id)}
					<Table.Row
						class="cursor-pointer"
						onclick={() => {
							if (currentLocation?.id == loc?.id) {
								callback(currentLocation);
								open = false;
							} else {
								currentLocation = loc;
							}
						}}
						data-state={currentLocation?.id === loc?.id && 'selected'}
					>
						<Table.Cell class="font-medium">{loc.id ?? 'N/A'}</Table.Cell>
						<Table.Cell>{loc.x ?? 'N/A'}</Table.Cell>
						<Table.Cell>{loc.y ?? 'N/A'}</Table.Cell>
						<Table.Cell>{loc.z ?? 'N/A'}</Table.Cell>
					</Table.Row>
				{/each}
			</Table.Body>
		</Table.Root>

		<Dialog.Footer>
			<div class="mt-8 mb-4 flex items-center justify-between gap-4 px-4">
				<div class="flex w-full items-center gap-8 lg:w-fit">
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
				<Button type="submit" onclick={submit}>Выбрать</Button>
			</div>
		</Dialog.Footer>
	</Dialog.Content>
</Dialog.Root>
