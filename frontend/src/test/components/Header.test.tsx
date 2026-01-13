import { describe, it, expect, beforeEach, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import Header from '../../components/Header'
import { AuthProvider } from '../../context/AuthContext'
import { CartProvider } from '../../context/CartContext'
import React from 'react'

/**
 * Header Tests
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

describe('Header Component', () => {
    beforeEach(() => {
        localStorage. clear()
        vi.clearAllMocks()
    })

    it('should render logo', () => {
        renderWithProviders(<Header />)
        const logo = screen.getByText('📚 BookStore')
        expect(logo).toBeInTheDocument()
    })

    it('should show login and register links for unauthenticated users', () => {
        renderWithProviders(<Header />)
        expect(screen.getByText('Login')).toBeInTheDocument()
        expect(screen.getByText('Register')).toBeInTheDocument()
    })

    it('should show cart link for authenticated users', () => {
        localStorage.setItem('bookstore_token', 'test_token')
        localStorage.setItem('bookstore_user', JSON.stringify({
            id: '1',
            username: 'testuser'
        }))

        renderWithProviders(<Header />)
        expect(screen.getByText(/Cart/)).toBeInTheDocument()
    })
})