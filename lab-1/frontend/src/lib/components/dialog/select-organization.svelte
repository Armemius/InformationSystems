<script lang="ts">
	import * as Table from '$lib/components/ui/table';
	import { Button } from '$lib/components/ui/button';
	import ChevronsLeftIcon from '@tabler/icons-svelte/icons/chevrons-left';
	import ChevronLeftIcon from '@tabler/icons-svelte/icons/chevron-left';
	import ChevronRightIcon from '@tabler/icons-svelte/icons/chevron-right';
	import ChevronsRightIcon from '@tabler/icons-svelte/icons/chevrons-right';
	import ws from '$lib/api/ws';
	import http from '$lib/api/http';
	import { toast } from 'svelte-sonner';
	import { onDestroy, onMount } from 'svelte';
	import type { Data } from '$lib/types/data';
	import { type Organization } from '$lib/types/organization';
	import * as Dialog from '$lib/components/ui/dialog/index.js';
	import type { SelectApi } from '$lib/types/togglers';
	import type { Callback } from '$lib/types/callback';

	let open = $state(false);
	let {
		expose = $bindable({}),
		callback = () => {}
	}: { expose: SelectApi; callback?: Callback<Organization> } = $props();

	expose.toggle = () => {
		currentOrganization = null;
		currentPage = 0;
		totalElements = 0;
		open = true;
		fetchData();
	};

	let currentOrganization = $state<Organization | null>(null);

	let currentPage = $state(0);
	let totalElements = $state(0);
	let pageSize = $state(10);
	let organizationList = $state<Organization[]>([]);
	const totalPages = $derived(Math.max(Math.ceil(totalElements / pageSize), 1));
	const prevPageAvailable = $derived(currentPage > 0);
	const nextPageAvailable = $derived(currentPage + 1 < totalPages);

	const submit = async () => {
		if (currentOrganization) {
			callback(currentOrganization);
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
			const resp = await http.get<Data<number>>('/management/organization/total');
			totalElements = resp.data.data;
			const respOrganization = await http.get<Organization[]>('/management/organization', {
				params: {
					page: currentPage,
					size: pageSize
				}
			});
			organizationList = respOrganization.data;
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
					<Table.Head>Имя</Table.Head>
					<Table.Head>Дата создания</Table.Head>
				</Table.Row>
			</Table.Header>
			<Table.Body>
				{#each organizationList as org (org.id)}
					<Table.Row
						class="cursor-pointer"
						onclick={() => {
							if (currentOrganization?.id == org?.id) {
								callback(currentOrganization);
								open = false;
							} else {
								currentOrganization = org;
							}
						}}
						data-state={currentOrganization?.id === org?.id && 'selected'}
					>
						<Table.Cell class="font-medium">{org.id ?? 'N/A'}</Table.Cell>
						<Table.Cell>{org.name ?? 'N/A'}</Table.Cell>
						<Table.Cell>{formatToRuDate(org.creationDate) ?? 'N/A'}</Table.Cell>
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
