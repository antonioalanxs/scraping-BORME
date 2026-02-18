<script lang="ts">
	export let message: string | null = null;
</script>

{#if message}
	<p>{message}</p>
{/if}
