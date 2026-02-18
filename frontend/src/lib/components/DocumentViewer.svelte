<script lang="ts">
	export let title: string;
	export let uri: string;
</script>

<iframe {title} src={`https://docs.google.com/gview?url=${encodeURIComponent(uri)}&embedded=true`}
></iframe>

<style>
	iframe {
		width: 100%;
		aspect-ratio: 16 / 9;
	}
</style>
