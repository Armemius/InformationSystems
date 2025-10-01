<script>
	import '../app.css';
	import { onMount } from 'svelte';
	import { authStore, loadUser } from '$lib/stores/user';
	import AuthForm from '$lib/components/auth/auth-form.svelte';
	import AppSidebar from '$lib/components/app-sidebar.svelte';
	import { ModeWatcher } from 'mode-watcher';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
	import Notifier from '$lib/components/notifications.svelte';

	let { children } = $props();

	onMount(() => {
		loadUser();
	});
</script>

<ModeWatcher />
{#if $authStore === undefined}
	<div class="flex h-screen w-full items-center justify-center text-2xl">Загрузка...</div>
{:else if $authStore === null}
	<div class="flex h-screen w-full items-center justify-center px-4">
		<AuthForm />
	</div>
{:else}
	<Notifier />
	<Sidebar.Provider>
		<AppSidebar />
		<Sidebar.Inset>
			{@render children()}
		</Sidebar.Inset>
	</Sidebar.Provider>
{/if}
