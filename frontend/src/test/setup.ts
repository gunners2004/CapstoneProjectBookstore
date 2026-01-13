import { afterEach, vi } from 'vitest'
import { cleanup } from '@testing-library/react'
import '@testing-library/jest-dom'

/**
 * Test Setup - выполняется перед каждым тестом
 */

// Очищаем DOM после каждого теста
afterEach(() => {
    cleanup()
})

// Мокируем localStorage
const localStorageMock = {
    getItem: vi.fn(),
    setItem: vi.fn(),
    removeItem: vi.fn(),
    clear: vi.fn()
}

Object.defineProperty(window, 'localStorage', {
    value: localStorageMock
})

// Мокируем fetch
global.fetch = vi.fn()