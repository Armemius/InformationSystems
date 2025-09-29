<script lang="ts" module>
	import AudioWaveformIcon from '@lucide/svelte/icons/audio-waveform';
	import BookOpenIcon from '@lucide/svelte/icons/book-open';
	import BoxIcon from '@lucide/svelte/icons/box';
	import LinkIcon from '@lucide/svelte/icons/link';
	import BriefcaseIcon from '@lucide/svelte/icons/briefcase';

	import ChartPieIcon from '@lucide/svelte/icons/chart-pie';
	import CommandIcon from '@lucide/svelte/icons/command';
	import FrameIcon from '@lucide/svelte/icons/frame';
	import GalleryVerticalEndIcon from '@lucide/svelte/icons/gallery-vertical-end';
	import MapIcon from '@lucide/svelte/icons/map';
	import Settings2Icon from '@lucide/svelte/icons/settings-2';
	import SquareTerminalIcon from '@lucide/svelte/icons/square-terminal';

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
	import NavProjects from './nav-projects.svelte';
	import NavUser from './nav-user.svelte';
	import TeamSwitcher from './team-switcher.svelte';
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
