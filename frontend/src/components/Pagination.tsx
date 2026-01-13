import React from 'react'
import '../styles/pagination.css'

/**
 * Pagination компонент
 */
interface PaginationProps {
    currentPage: number
    totalPages: number
    onPageChange: (page: number) => void
}

export default function Pagination({
                                       currentPage,
                                       totalPages,
                                       onPageChange
                                   }: PaginationProps) {
    const pages = Array.from({ length: totalPages }, (_, i) => i)

    return (
        <div className="pagination">
            {/* Кнопка Предыдущая */}
            <button
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
                className="pagination-btn"
            >
                ← Previous
            </button>

            {/* Номера страниц */}
            <div className="page-numbers">
                {pages. map(page => (
                    <button
                        key={page}
                        onClick={() => onPageChange(page)}
                        className={`page-btn ${page === currentPage ? 'active' : ''}`}
                    >
                        {page + 1}
                    </button>
                ))}
            </div>

            {/* Кнопка Следующая */}
            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
                className="pagination-btn"
            >
                Next →
            </button>
        </div>
    )
}