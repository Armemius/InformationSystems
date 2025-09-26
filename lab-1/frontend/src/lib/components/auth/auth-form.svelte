<script lang="ts">
	import * as Card from '$lib/components/ui/card/index.js';

	import LoginContent from '$lib/components/auth/login-content.svelte';
	import RegisterContent from '$lib/components/auth/register-content.svelte';

	let isLogin = $state(true);

	const changeMode = () => {
		isLogin = !isLogin;
	};
</script>

<Card.Root class="mx-auto w-full max-w-sm">
	<Card.Header>
		<Card.Title class="text-2xl">Вход</Card.Title>
		<Card.Description>Введите свои данные чтобы продолжить</Card.Description>
	</Card.Header>
	<Card.Content>
		{#if isLogin}
			<LoginContent {changeMode} />
		{:else}
			<RegisterContent {changeMode} />
		{/if}
	</Card.Content>
</Card.Root>
