<script lang="ts">
	import { onMount } from 'svelte';
	import { page } from '$app/stores';

	import { findConstitutionById } from '$lib/api/constitutions';
	import type { Constitution } from '$lib/types/constitution';
	import { requestState } from '$lib/utilities/requests/requestState';
	import Spinner from '$lib/components/Spinner.svelte';
	import Error from '$lib/components/Error.svelte';
	import DocumentViewer from '$lib/components/DocumentViewer.svelte';

	$: id = $page.params.id;

	const { data, isLoading, errorMessage, execute } = requestState<Constitution>();

	onMount(async () => id && (await execute(() => findConstitutionById(id))));
</script>

<a href="/">Back</a>

<Spinner condition={$isLoading} />
<Error message={$errorMessage} />

{#if $data}
	<pre>{JSON.stringify($data, null, 2)}</pre>
	<DocumentViewer title={`${$data?.entity?.name} source`} uri={$data.source} />
{/if}
