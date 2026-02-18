import path from 'path';

import { defineConfig } from 'vite';
import { sveltekit } from '@sveltejs/kit/vite';

export default defineConfig({
	plugins: [sveltekit()],
	envDir: path.resolve(__dirname, '../'),
	resolve: {
		alias: {
			$lib: path.resolve('./src/lib'),
			$routes: path.resolve('./src/routes')
		}
	}
});
