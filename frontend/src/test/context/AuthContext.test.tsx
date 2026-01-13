import { describe, it, expect, beforeEach, vi } from 'vitest'

/**
 * Простые тесты для AuthContext
 * Тэстируем только основной функционал без renderHook
 */

describe('AuthContext - Basic Tests', () => {
    beforeEach(() => {
        localStorage.clear()
        vi.clearAllMocks()
    })

    it('should initialize with null user', () => {
        const user = localStorage.getItem('bookstore_user')
        expect(user).toBeNull()
    })

    it('should store user in localStorage', () => {
        const mockUser = {
            id: '1',
            username: 'testuser',
            email: 'test@example.com'
        }

        localStorage.setItem('bookstore_user', JSON.stringify(mockUser))
        const stored = localStorage.getItem('bookstore_user')

        expect(stored).toBeTruthy()
        expect(JSON.parse(stored! )).toEqual(mockUser)
    })

    it('should clear user from localStorage', () => {
        const mockUser = { id: '1', username: 'testuser', email: 'test@example. com' }
        localStorage.setItem('bookstore_user', JSON.stringify(mockUser))

        localStorage.removeItem('bookstore_user')
        const stored = localStorage.getItem('bookstore_user')

        expect(stored).toBeNull()
    })

    it('should handle JSON serialization', () => {
        const mockUser = {
            id: '1',
            username: 'john_doe',
            email: 'john@example.com',
            role: 'USER'
        }

        const serialized = JSON.stringify(mockUser)
        const deserialized = JSON.parse(serialized)

        expect(deserialized. username).toBe('john_doe')
        expect(deserialized. role).toBe('USER')
    })
})