<script lang="ts">
	import { onDestroy, onMount } from 'svelte';
	import { Toaster } from './ui/sonner';
	import ws from '$lib/api/ws';
	import { toast } from 'svelte-sonner';

	let disconnect: (() => void) | null = null;

	onMount(() => {
		disconnect = ws.subscribe(() => {
			toast('В предметную область были внесены изменения!');
		});
    toast("Соединение с сервером установлено!")
	});

	onDestroy(() => {
		if (disconnect) {
			disconnect();
		}
	});
</script>

<Toaster />
