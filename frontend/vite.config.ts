
import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import path from 'path'

/**
 * Vitest Configuration
 */
export default defineConfig({
    plugins: [react()],
    test: {
        // JSDOM environment for DOM testing
        environment: 'jsdom',

        // Setup files
        setupFiles: ['./src/__tests__/setup.ts'],

        // Global test functions (no need to import)
        globals: true,

        // Timeout for each test
        testTimeout: 10000,

        // Coverage
        coverage: {
            provider: 'v8',
            reporter: ['text', 'json', 'html'],
            exclude:  [
                'node_modules/',
                'src/__tests__/',
                '**/*. test.ts',
                '**/*.test.tsx'
            ],
            statements: 80,
            branches: 75,
            functions: 80,
            lines: 80
        }
    },
    resolve: {
        alias: {
            '@': path. resolve(__dirname, './src')
        }
    }
})