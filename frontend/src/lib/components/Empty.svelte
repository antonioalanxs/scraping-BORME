<script lang="ts">
	export let condition: boolean = false;
	export let message: string = 'No data available.';
</script>

{#if condition}
	<p>{message}</p>
{/if}
