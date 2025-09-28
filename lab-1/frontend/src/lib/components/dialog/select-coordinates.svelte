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
	import { type Coordinates } from '$lib/types/coordinates';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import EditCoordinatesDialog from '$lib/components/dialog/edit-coordinates.svelte';
	import CreateCoordinatesDialog from '$lib/components/dialog/create-coordinates.svelte';
	import ShowCoordinatesDialog from '$lib/components/dialog/show-coordinates.svelte';

	let open = $state(false);
	let { expose = $bindable({}), callback = (coord: Coordinates) => {} } = $props();

	expose.toggle = () => {
		currentCoord = null;
		currentPage = 0;
		totalElements = 0;
		open = true;
		fetchData();
	};

	let currentCoord = $state<Coordinates | null>(null);

	let currentPage = $state(0);
	let totalElements = $state(0);
	let pageSize = $state(10);
	let coordinatesList = $state<Coordinates[]>([]);
	const totalPages = $derived(Math.max(Math.ceil(totalElements / pageSize), 1));
	const prevPageAvailable = $derived(currentPage > 0);
	const nextPageAvailable = $derived(currentPage + 1 < totalPages);

	const submit = async () => {
		if (currentCoord) {
			callback(currentCoord);
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
			const resp = await http.get<Data<number>>('/management/coordinates/total');
			totalElements = resp.data.data;
			const respCoordinates = await http.get<Coordinates[]>('/management/coordinates', {
				params: {
					page: currentPage,
					size: pageSize
				}
			});
			coordinatesList = respCoordinates.data;
		} catch (ex) {
			console.error('Error while fetching coordinates', ex);
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
			<Dialog.Title>Выбор координаты</Dialog.Title>
			<Dialog.Description
				>Меню для выбора существующей координаты для последующего использования в других объектах</Dialog.Description
			>
		</Dialog.Header>
		<Table.Root>
			<Table.Header>
				<Table.Row>
					<Table.Head class="w-[100px]">ID</Table.Head>
					<Table.Head>X</Table.Head>
					<Table.Head>Y</Table.Head>
				</Table.Row>
			</Table.Header>
			<Table.Body>
				{#each coordinatesList as coord (coord.id)}
					<Table.Row
						class="cursor-pointer"
						onclick={() => {
							if (currentCoord?.id == coord?.id) {
								callback(currentCoord);
								open = false;
							} else {
								currentCoord = coord;
							}
						}}
						data-state={currentCoord?.id === coord?.id && 'selected'}
					>
						<Table.Cell class="font-medium">{coord.id ?? 'N/A'}</Table.Cell>
						<Table.Cell>{coord.x ?? 'N/A'}</Table.Cell>
						<Table.Cell>{coord.y ?? 'N/A'}</Table.Cell>
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
