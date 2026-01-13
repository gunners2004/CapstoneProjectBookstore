import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import BookCard from '../../components/BookCard'

/**
 * Простые тесты для BookCard компонента
 */

describe('BookCard Component', () => {
    const mockBook = {
        id: '1',
        title: 'The Great Gatsby',
        author:  'F. Scott Fitzgerald',
        price: 15.99,
        discountPrice: 12.99,
        imageUrl: 'https://example.com/gatsby.jpg',
        averageRating: 4.5,
        ratingCount: 250,
        quantityInStock: 10
    }

    const mockOnAddToCart = vi.fn()
    const mockOnAddToFavorites = vi.fn()

    it('should render book title', () => {
        render(
            <BookCard
                {... mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('The Great Gatsby')).toBeInTheDocument()
    })

    it('should render author name', () => {
        render(
            <BookCard
                {... mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('F. Scott Fitzgerald')).toBeInTheDocument()
    })

    it('should render current price', () => {
        render(
            <BookCard
                {...mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('$12.99')).toBeInTheDocument()
    })

    it('should show "In Stock" when quantity > 0', () => {
        render(
            <BookCard
                {...mockBook}
                quantityInStock={10}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText('In Stock')).toBeInTheDocument()
    })

    it('should show "Out of Stock" when quantity = 0', () => {
        render(
            <BookCard
                {...mockBook}
                quantityInStock={0}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen. getByText('Out of Stock')).toBeInTheDocument()
    })

    it('should call onAddToCart when button clicked', async () => {
        const user = userEvent.setup()

        render(
            <BookCard
                {...mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        const addButton = screen.getByText('Add to Cart')
        await user.click(addButton)

        expect(mockOnAddToCart).toHaveBeenCalledTimes(1)
    })

    it('should render rating information', () => {
        render(
            <BookCard
                {... mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        expect(screen.getByText(/4.5/)).toBeInTheDocument()
        expect(screen.getByText(/250/)).toBeInTheDocument()
    })

    it('should have correct image src', () => {
        render(
            <BookCard
                {... mockBook}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        const image = screen.getByAltText('The Great Gatsby') as HTMLImageElement
        expect(image.src).toBe('https://example.com/gatsby.jpg')
    })

    it('should disable button when out of stock', () => {
        render(
            <BookCard
                {...mockBook}
                quantityInStock={0}
                onAddToCart={mockOnAddToCart}
                onAddToFavorites={mockOnAddToFavorites}
            />
        )

        const button = screen.getByText('Out of Stock')
        expect(button).toBeDisabled()
    })

    it('should calculate discount percentage correctly', () => {
        const originalPrice = 100
        const discountPrice = 75
        const discount = Math.round((1 - (discountPrice / originalPrice)) * 100)

        expect(discount).toBe(25)
    })
})