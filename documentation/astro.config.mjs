// documentation/astro.config.mjs
import { defineConfig } from 'astro/config';
import tailwind from '@astrojs/tailwind';

export default defineConfig({
  output: 'static',
  integrations: [tailwind()],
  // IMPORTANT: do NOT set outDir here; let Astro use the default "dist"
  // If you previously had: outDir: './docs' — remove it.
  // No site/base needed for Vercel.
});
