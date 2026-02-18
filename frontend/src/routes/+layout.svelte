<script lang="ts">
	import '$routes/layout.css';
	import favicon from '$lib/assets/favicon.svg';
	import '@picocss/pico';

	let { children } = $props();
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

{@render children()}
