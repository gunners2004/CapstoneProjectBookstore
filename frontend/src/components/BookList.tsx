import { useState, useEffect } from 'react'
import BookCard from './BookCard'
import Pagination from './Pagination'
import * as bookAPI from '../api/book'
import { useCart } from '../context/CartContext'
import '../styles/book-list.css'

/**
 * BookList компонент
 *
 * Отображает список книг с пагинацией
 */
interface BookListProps {
    search?: string
    genre?: string
    author?: string
}

export default function BookList({ search, genre, author }: BookListProps) {
    const [books, setBooks] = useState<any[]>([])
    const [totalPages, setTotalPages] = useState(0)
    const [currentPage, setCurrentPage] = useState(0)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const { addToCart } = useCart()

    const size = 12 // Количество книг на странице

    /**
     * Загрузить книги
     */
    useEffect(() => {
        const loadBooks = async () => {
            try {
                setLoading(true)
                setError(null)

                let response

                if (search) {
                    response = await bookAPI.searchBooks(search, currentPage, size)
                } else if (genre) {
                    response = await bookAPI.searchByGenre(genre, currentPage, size)
                } else if (author) {
                    response = await bookAPI.searchByAuthor(author, currentPage, size)
                } else {
                    response = await bookAPI.getAllBooks(currentPage, size)
                }

                setBooks(response.content)
                setTotalPages(response.totalPages)
            } catch (err:  any) {
                setError(err.message || 'Failed to load books')
            } finally {
                setLoading(false)
            }
        }

        loadBooks()
    }, [search, genre, author, currentPage])

    const handleAddToCart = async (bookId: string) => {
        const success = await addToCart(bookId, 1)
        if (success) {
            alert('Book added to cart!')
        }
    }

    if (loading) {
        return <div className="loading">Loading books...</div>
    }

    if (error) {
        return <div className="error">{error}</div>
    }

    if (books.length === 0) {
        return <div className="empty-state">No books found</div>
    }

    return (
        <div className="book-list-container">
            <div className="book-list">
                {books.map(book => (
                    <BookCard
                        key={book.id}
                        id={book.id}
                        title={book.title}
                        author={book.author}
                        price={book.price}
                        discountPrice={book.discountPrice}
                        imageUrl={book. imageUrl}
                        averageRating={book.averageRating}
                        ratingCount={book.ratingCount}
                        quantityInStock={book.quantityInStock}
                        onAddToCart={() => handleAddToCart(book. id)}
                    />
                ))}
            </div>

            {totalPages > 1 && (
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                />
            )}
        </div>
    )
}