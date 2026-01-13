import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import BookCard from '../../components/BookCard'
import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from '../../context/AuthContext'
import React from 'react'

/**
 * BookCard Tests
 */
const renderWithRouter = (component: React.ReactElement) => {
    return render(
        <BrowserRouter>
            <AuthProvider>
                {component}
            </AuthProvider>
        </BrowserRouter>
    )
}

describe('BookCard Component', () => {
    const mockBook = {
        id:  '1',
        title: 'Test Book',
        author:  'Test Author',
        price: 29.99,
        discountPrice: 19.99,
        imageUrl: 'https://example.com/image.jpg',
        averageRating: 4.5,
        ratingCount: 100,
        quantityInStock: 10
    }

    const mockOnAddToCart = vi.fn()
    const mockOnAddToFavorites = vi.fn()

    it('should render book information', () => {
        renderWithRouter(
            <BookCard
                {... mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('Test Book')).toBeInTheDocument()
        expect(screen.getByText('Test Author')).toBeInTheDocument()
        expect(screen.getByText('$19.99')).toBeInTheDocument()
    })

    it('should show discount badge', () => {
        renderWithRouter(
            <BookCard
                {...mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText(/-\d+%/)).toBeInTheDocument()
    })

    it('should show in stock status', () => {
        renderWithRouter(
            <BookCard
                {...mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('In Stock')).toBeInTheDocument()
    })

    it('should call onAddToCart when button clicked', async () => {
        const user = userEvent.setup()
        renderWithRouter(
            <BookCard
                {...mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        const addButton = screen.getByText('Add to Cart')
        await user.click(addButton)

        expect(mockOnAddToCart).toHaveBeenCalled()
    })
})