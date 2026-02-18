<script lang="ts">
	import { goto } from '$app/navigation';

	import type { Constitution } from '$lib/types/constitution';

	export let constitutions: Constitution[] | null = null;
</script>

{#if constitutions && constitutions.length > 0}
	<div class="overflow-auto">
		<table class="striped">
			<thead>
				<tr>
					<th>Code</th>
					<th>Name</th>
					<th>Source</th>
				</tr>
			</thead>

			<tbody>
				{#each constitutions as constitution}
					<tr on:click={() => goto(`/constitutions/${constitution.id}`)}>
						<td>{constitution.entity.code}</td>
						<td>{constitution.entity.name}</td>
						<td>
							<a
								href={constitution.source}
								target="_blank"
								rel="noopener noreferrer"
								on:click|stopPropagation
							>
								{constitution.source}
							</a>
						</td>
					</tr>
				{/each}
			</tbody>
		</table>
	</div>
{/if}

<style>
	tr {
		cursor: pointer;
	}
</style>
