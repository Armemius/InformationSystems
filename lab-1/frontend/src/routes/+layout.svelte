<script>
	import '../app.css';
	import { onMount } from 'svelte';
	import { authStore, loadUser } from '$lib/stores/user';
	import AuthForm from '$lib/components/auth/auth-form.svelte';

  let { children } = $props();

	onMount(() => {
		loadUser();
	});
</script>

{#if $authStore === undefined}
	<div class="flex h-screen w-full items-center justify-center text-2xl">
    Загрузка...
  </div>
{:else if $authStore === null}
	<div class="flex h-screen w-full items-center justify-center px-4">
		<AuthForm />
	</div>
{:else}
	{@render children()}
{/if}
