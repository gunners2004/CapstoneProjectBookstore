import client from './client'

/**
 * API функции для работы с книгами
 */

export interface Book {
    id: string
    title: string
    author: string
    description: string
    price: number
    discountPrice?: number
    genre: string
    imageUrl:  string
    quantityInStock: number
    averageRating: number
    ratingCount: number
    featured: boolean
}

export interface BookResponse {
    content: Book[]
    totalElements:  number
    totalPages: number
    currentPage: number
}

export async function getAllBooks(page = 0, size = 10): Promise<BookResponse> {
    const response = await client.get(`/books?page=${page}&size=${size}`)
    return response.data
}

export async function searchBooks(title: string, page = 0, size = 10): Promise<BookResponse> {
    const response = await client. get(`/books/search?title=${title}&page=${page}&size=${size}`)
    return response.data
}

export async function searchByAuthor(author: string, page = 0, size = 10): Promise<BookResponse> {
    const response = await client.get(`/books/search/author?author=${author}&page=${page}&size=${size}`)
    return response.data
}

export async function searchByGenre(genre: string, page = 0, size = 10): Promise<BookResponse> {
    const response = await client. get(`/books/search/genre?genre=${genre}&page=${page}&size=${size}`)
    return response.data
}

export async function getFeaturedBooks(page = 0, size = 10): Promise<BookResponse> {
    const response = await client.get(`/books/featured?page=${page}&size=${size}`)
    return response.data
}

export async function getBookById(id: string): Promise<Book> {
    const response = await client.get(`/books/${id}`)
    return response.data
}

export async function createBook(data: any): Promise<Book> {
    const response = await client.post('/books', data)
    return response.data
}

export async function updateBook(id: string, data: any): Promise<Book> {
    const response = await client.put(`/books/${id}`, data)
    return response.data
}

export async function deleteBook(id: string): Promise<void> {
    await client.delete(`/books/${id}`)
}