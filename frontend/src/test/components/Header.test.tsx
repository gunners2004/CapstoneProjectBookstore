import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import Header from '../../components/Header'

/**
 * Простые тесты для Header компонента
 */

describe('Header Component', () => {
    const renderHeader = () => {
        return render(
            <BrowserRouter>
                <Header />
            </BrowserRouter>
        )
    }

    it('should render logo text', () => {
        renderHeader()
        expect(screen.getByText(/📚 BookStore/)).toBeInTheDocument()
    })

    it('should render search input', () => {
        renderHeader()
        const searchInput = screen.getByPlaceholderText(/Search books/)
        expect(searchInput).toBeInTheDocument()
    })

    it('should render home link', () => {
        renderHeader()
        const homeLink = screen.getByText('Home')
        expect(homeLink).toBeInTheDocument()
    })

    it('should render books link', () => {
        renderHeader()
        const booksLink = screen.getByText('Books')
        expect(booksLink).toBeInTheDocument()
    })

    it('should have correct home link href', () => {
        renderHeader()
        const homeLink = screen.getByText('Home').closest('a')
        expect(homeLink?.getAttribute('href')).toBe('/')
    })

    it('should have correct books link href', () => {
        renderHeader()
        const booksLink = screen.getByText('Books').closest('a')
        expect(booksLink?.getAttribute('href')).toBe('/books')
    })

    it('search input should be empty initially', () => {
        renderHeader()
        const searchInput = screen.getByPlaceholderText(/Search books/) as HTMLInputElement
        expect(searchInput.value).toBe('')
    })
})