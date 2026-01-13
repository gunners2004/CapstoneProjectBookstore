
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

/**
 * Vite Configuration
 *
 * Использует автоматический JSX transform (React 17+)
 */
export default defineConfig({
    plugins: [react()],

    server: {
        port: 5173,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '/api')
            }
        }
    },

    resolve: {
        alias:  {
            '@': path.resolve(__dirname, './src')
        }
    }
})