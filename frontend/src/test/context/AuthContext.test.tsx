import { describe, it, expect, beforeEach, vi } from 'vitest'
import { renderHook, act, waitFor } from '@testing-library/react'
import { AuthProvider, useAuth } from '../../context/AuthContext'
import React from 'react'

/**
 * AuthContext Tests
 */
describe('AuthContext', () => {
    beforeEach(() => {
        localStorage.clear()
        vi.clearAllMocks()
    })

    it('should initialize with null token', () => {
        const wrapper = ({ children }: any) =>
            React.createElement(AuthProvider, null, children)
        const { result } = renderHook(() => useAuth(), { wrapper })

        expect(result.current.token).toBeNull()
        expect(result.current.isAuthenticated).toBe(false)
    })

    it('should register user successfully', async () => {
        global.fetch = vi.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                token: 'test_token',
                userId: '123',
                username: 'testuser'
            })
        })

        const wrapper = ({ children }: any) =>
            React.createElement(AuthProvider, null, children)
        const { result } = renderHook(() => useAuth(), { wrapper })

        let success = false
        await act(async () => {
            success = await result.current.register({
                username: 'testuser',
                email: 'test@example.com',
                password: 'password123',
                firstName: 'Test',
                lastName: 'User'
            })
        })

        expect(success).toBe(true)
        expect(result.current.token).toBe('test_token')
    })

    it('should login user successfully', async () => {
        global.fetch = vi.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                token: 'login_token',
                userId: '456',
                username: 'testuser'
            })
        })

        const wrapper = ({ children }: any) =>
            React.createElement(AuthProvider, null, children)
        const { result } = renderHook(() => useAuth(), { wrapper })

        let success = false
        await act(async () => {
            success = await result.current.login('test@example.com', 'password123')
        })

        expect(success).toBe(true)
        expect(result.current. token).toBe('login_token')
    })

    it('should logout user', async () => {
        const wrapper = ({ children }: any) =>
            React.createElement(AuthProvider, null, children)
        const { result } = renderHook(() => useAuth(), { wrapper })

        // Сначала логиним
        global.fetch = vi.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                token: 'test_token',
                userId: '123',
                username: 'testuser'
            })
        })

        await act(async () => {
            await result.current.login('test@example.com', 'password123')
        })

        expect(result.current.isAuthenticated).toBe(true)

        // Затем логаутим
        act(() => {
            result.current. logout()
        })

        expect(result.current.token).toBeNull()
        expect(result.current.isAuthenticated).toBe(false)
    })
})