<script lang="ts">
	export let condition: boolean = false;
</script>

{#if condition}
	<span aria-busy="true">Loading...</span>
{/if}
