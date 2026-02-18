import { writable } from 'svelte/store';

export function requestState<T>() {
	const data = writable<T | null>(null);
	const isLoading = writable(false);
	const errorMessage = writable<string | null>(null);

	async function execute(request: () => Promise<T>): Promise<boolean> {
		isLoading.set(true);
		errorMessage.set(null);

		try {
			const result = await request();
			data.set(result);
			return true;
		} catch (error) {
			errorMessage.set(error as string);
			return false;
		} finally {
			isLoading.set(false);
		}
	}

	function reset() {
		data.set(null);
		errorMessage.set(null);
	}

	return {
		data,
		isLoading,
		errorMessage,
		execute,
		reset
	};
}
