import { describe, it, expect, beforeEach, vi } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import Login from '../../pages/Login'
import { AuthProvider } from '../../context/AuthContext'
import { CartProvider } from '../../context/CartContext'
import React from 'react'

/**
 * Login Page Tests
 */
const renderWithProviders = (component: React.ReactElement) => {
    return render(
        <BrowserRouter>
            <AuthProvider>
                <CartProvider>
                    {component}
                </CartProvider>
            </AuthProvider>
        </BrowserRouter>
    )
}

describe('Login Page', () => {
    beforeEach(() => {
        localStorage.clear()
        vi.clearAllMocks()
        global.fetch = vi.fn()
    })

    it('should render login form', () => {
        renderWithProviders(<Login />)

        expect(screen.getByText('Login to Your Account')).toBeInTheDocument()
        expect(screen.getByPlaceholderText('your@email.com')).toBeInTheDocument()
        expect(screen.getByPlaceholderText('••••••••')).toBeInTheDocument()
    })

    it('should show error when fields are empty', async () => {
        const user = userEvent.setup()
        renderWithProviders(<Login />)

        const loginButton = screen.getByRole('button', { name: /login/i })
        await user.click(loginButton)

        await waitFor(() => {
            expect(screen.getByText(/please fill in all fields/i)).toBeInTheDocument()
        })
    })

    it('should login successfully', async () => {
        const user = userEvent.setup()
        global.fetch = vi.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => ({
                token: 'test_token',
                userId: '1',
                username: 'testuser'
            })
        })

        renderWithProviders(<Login />)

        const emailInput = screen.getByPlaceholderText('your@email. com')
        const passwordInput = screen.getByPlaceholderText('••••••••')
        const loginButton = screen.getByRole('button', { name: /login/i })

        await user.type(emailInput, 'test@example.com')
        await user.type(passwordInput, 'password123')
        await user.click(loginButton)

        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledWith(
                expect.stringContaining('/api/auth/login'),
                expect.any(Object)
            )
        })
    })
})