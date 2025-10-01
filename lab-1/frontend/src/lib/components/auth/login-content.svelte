<script lang="ts">
	import http from '$lib/api/http';
	import { Button } from '$lib/components/ui/button/index.js';
	import { Input } from '$lib/components/ui/input/index.js';
	import { Label } from '$lib/components/ui/label/index.js';
	import { authStore } from '$lib/stores/user';
	import type User from '$lib/types/user';

	let { changeMode } = $props();

	let username: string = $state('');
	let password: string = $state('');
	let errorMessage: string = $state('');
	let loading: boolean = $state(false);

	const validateInput = () => {
		if (!username) {
			errorMessage = 'Введите имя пользователя';
			return false;
		}
		if (!password) {
			errorMessage = 'Введите пароль';
			return false;
		}
		if (username.length < 5) {
			errorMessage = 'Слишком короткое имя пользователя';
			return false;
		}
		if (username.length > 16) {
			errorMessage = 'Слишком длинное имя пользователя';
			return false;
		}
		if (password.length < 7) {
			errorMessage = 'Слишком короткий пароль';
			return false;
		}
		if (password.length > 255) {
			errorMessage = 'Слишком длинный пароль';
			return false;
		}
		errorMessage = '';
		return true;
	};

	const submit = async () => {
		if (!validateInput() || loading) {
			return;
		}
		try {
			loading = true;
			const response = await http.post<User>('/auth/login', {
				username,
				password
			});
			authStore.set(response.data);
		} catch (ex) {
			errorMessage = 'Неверные данные пользователя';
			console.error(ex);
		} finally {
			loading = false;
		}
	};
</script>

<div class="grid gap-4">
	<div class="grid gap-2">
		<Label for="email">Имя пользователя</Label>
		<Input bind:value={username} id="email" type="text" placeholder="example@site.ru" required />
	</div>
	<div class="grid gap-2">
		<div class="flex items-center">
			<Label for="password">Пароль</Label>
		</div>
		<Input bind:value={password} id="password" type="password" placeholder="*****" required />
	</div>
	<Button type="submit" onclick={submit} class="w-full">Вход</Button>
	<div class="flex justify-center text-xs text-red-700">
		{errorMessage}
	</div>
</div>
<div class="mt-4 text-center text-sm">
	Нет аккаунта?
	<button onclick={changeMode} class="cursor-pointer underline"> Зарегистрироваться </button>
</div>
