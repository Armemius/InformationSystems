<script lang="ts" module>
	import BoxIcon from '@lucide/svelte/icons/box';
	import LinkIcon from '@lucide/svelte/icons/link';
	import BriefcaseIcon from '@lucide/svelte/icons/briefcase';

	const data = {
		navMain: [
			{
				title: 'Предметная область',
				url: '#',
				icon: BoxIcon,
				isActive: true,
				items: [
					{
						title: 'Организации',
						url: '/management/organizations'
					},
					{
						title: 'Координаты',
						url: '/management/coordinates'
					},
					{
						title: 'Локации',
						url: '/management/locations'
					},
					{
						title: 'Адреса',
						url: '/management/addresses'
					}
				]
			},
			{
				title: 'Управление',
				url: '#',
				icon: BriefcaseIcon,
				isActive: true,
				items: [
					// {
					// 	title: 'Карта',
					// 	url: '/map'
					// },
					{
						title: 'Операции',
						url: '/operations'
					}
				]
			},
			{
				title: 'Исходники',
				url: '#',
				icon: LinkIcon,
				items: [
					{
						title: 'Репозиторий с кодом',
						url: 'https://github.com/Armemius/InformationSystems/'
					},
					{
						title: 'Frontend на GitHub',
						url: 'https://github.com/Armemius/InformationSystems/tree/main/lab-1/frontend'
					},
					{
						title: 'Backend на GitHub',
						url: 'https://github.com/Armemius/InformationSystems/tree/main/lab-1/backend'
					},
					{
						title: 'Документация',
						url: API_URL + 'docs/'
					}
				]
			}
		]
	};
</script>

<script lang="ts">
	import NavMain from './nav-main.svelte';
	import NavUser from './nav-user.svelte';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import type { ComponentProps } from 'svelte';
	import { authStore } from '$lib/stores/user';
	import { API_URL } from '$lib/config';

	let {
		ref = $bindable(null),
		collapsible = 'icon',
		...restProps
	}: ComponentProps<typeof Sidebar.Root> = $props();
</script>

<Sidebar.Root {collapsible} {...restProps}>
	<Sidebar.Content>
		<NavMain items={data.navMain} />
	</Sidebar.Content>
	<Sidebar.Footer>
		<NavUser
			user={{
				name: $authStore!.username,
				role: $authStore!.role === 'ROLE_USER' ? 'Пользователь' : 'Админинстратор',
				avatar: '/avatars/shadcn.jpg'
			}}
		/>
	</Sidebar.Footer>
	<Sidebar.Rail />
</Sidebar.Root>
