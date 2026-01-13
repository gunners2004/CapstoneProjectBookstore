import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import Pagination from '../../components/Pagination'

/**
 * Простые тесты для Pagination компонента
 */

describe('Pagination Component', () => {
    const mockOnPageChange = vi.fn()

    it('should render previous button', () => {
        render(
            <Pagination
                currentPage={0}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        expect(screen.getByText(/Previous/)).toBeInTheDocument()
    })

    it('should render next button', () => {
        render(
            <Pagination
                currentPage={0}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        expect(screen.getByText(/Next/)).toBeInTheDocument()
    })

    it('should render page numbers', () => {
        render(
            <Pagination
                currentPage={0}
                totalPages={3}
                onPageChange={mockOnPageChange}
            />
        )

        expect(screen. getByText('1')).toBeInTheDocument()
        expect(screen.getByText('2')).toBeInTheDocument()
        expect(screen.getByText('3')).toBeInTheDocument()
    })

    it('should disable previous button on first page', () => {
        render(
            <Pagination
                currentPage={0}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const prevButton = screen.getByText(/Previous/)
        expect(prevButton).toBeDisabled()
    })

    it('should disable next button on last page', () => {
        render(
            <Pagination
                currentPage={4}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const nextButton = screen.getByText(/Next/)
        expect(nextButton).toBeDisabled()
    })

    it('should call onPageChange when page number clicked', async () => {
        const user = userEvent.setup()

        render(
            <Pagination
                currentPage={0}
                totalPages={3}
                onPageChange={mockOnPageChange}
            />
        )

        const page2Button = screen.getByText('2')
        await user.click(page2Button)

        expect(mockOnPageChange).toHaveBeenCalledWith(1)
    })

    it('should call onPageChange when next button clicked', async () => {
        const user = userEvent.setup()

        render(
            <Pagination
                currentPage={0}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const nextButton = screen.getByText(/Next/)
        await user.click(nextButton)

        expect(mockOnPageChange).toHaveBeenCalledWith(1)
    })

    it('should call onPageChange when previous button clicked', async () => {
        const user = userEvent.setup()

        render(
            <Pagination
                currentPage={2}
                totalPages={5}
                onPageChange={mockOnPageChange}
            />
        )

        const prevButton = screen.getByText(/Previous/)
        await user.click(prevButton)

        expect(mockOnPageChange).toHaveBeenCalledWith(1)
    })

    it('should highlight current page', () => {
        render(
            <Pagination
                currentPage={1}
                totalPages={3}
                onPageChange={mockOnPageChange}
            />
        )

        const page2Button = screen.getByText('2').parentElement
        expect(page2Button?. className).toContain('active')
    })
})