<script lang="ts">
	import { findConstitutionsByDate } from '$lib/api/constitutions';
	import type { Constitution } from '$lib/types/constitution';
	import { requestState } from '$lib/utilities/requests/requestState';
	import Spinner from '$lib/components/Spinner.svelte';
	import Error from '$lib/components/Error.svelte';
	import Empty from '$lib/components/Empty.svelte';
	import Table from '$lib/components/constitutions/Table.svelte';

	let date = '';
	let hasSearched = false;

	const { data, isLoading, errorMessage, execute } = requestState<Constitution[]>();

	async function handleClick() {
		if (!date) return;
		hasSearched = await execute(() => findConstitutionsByDate(date));
	}
</script>

<form>
	<input type="date" bind:value={date} />
	<button type="submit" on:click={handleClick} disabled={$isLoading}>Submit</button>
</form>

<Spinner condition={$isLoading} />
<Error message={$errorMessage} />
<Empty condition={hasSearched && !$isLoading && $data?.length === 0} />

<Table constitutions={$data} />
