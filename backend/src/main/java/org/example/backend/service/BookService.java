package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dto.BookDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Book;
import org.example.backend.repository.BookRepository;
import org. springframework.data.domain.Page;
import org.springframework.data.domain. Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


/**
 * Book Service
 */
@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Получить все книги (с пагинацией)
     */
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * Получить книгу по ID
     */
    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new CustomException("Book not found", 404));
    }

    /**
     * Поиск книг по названию
     */
    public Page<Book> searchByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleIgnoreCaseContaining(title, pageable);
    }

    /**
     * Поиск книг по автору
     */
    public Page<Book> searchByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthorIgnoreCase(author, pageable);
    }

    /**
     * Поиск книг по жанру
     */
    public Page<Book> searchByGenre(String genre, Pageable pageable) {
        return bookRepository.findByGenreIgnoreCase(genre, pageable);
    }

    /**
     * Избранные книги
     */
    public Page<Book> getFeaturedBooks(Pageable pageable) {
        return bookRepository. findByFeaturedTrue(pageable);
    }

    /**
     * Книги в наличии
     */
    public Page<Book> getBooksInStock(Pageable pageable) {
        return bookRepository.findByInStockTrue(pageable);
    }

    /**
     * Создать новую книгу (только админ)
     */
    public Book createBook(BookDTO bookDTO) {
        if (bookRepository.findByIsbn(bookDTO.getIsbn()) != null) {
            throw new CustomException("Book with this ISBN already exists", 409);
        }

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO. getPublisher());
        book.setIsbn(bookDTO.getIsbn());
        book.setPrice(bookDTO.getPrice());
        book.setDiscountPrice(bookDTO.getDiscountPrice());
        book.setPages(bookDTO.getPages());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setQuantityInStock(bookDTO.getQuantityInStock());
        book.setInStock(bookDTO.getQuantityInStock() > 0);
        book.setImageUrl(bookDTO.getImageUrl());
        book.setFeatured(bookDTO.isFeatured());
        book.setActive(bookDTO.isActive());
        book.setAverageRating(0);
        book.setRatingCount(0);
        book.setViewCount(0);
        book.setPurchaseCount(0);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        return bookRepository.save(book);
    }

    /**
     * Обновить книгу (только админ)
     */
    public Book updateBook(String id, BookDTO bookDTO) {
        Book book = getBookById(id);
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setGenre(bookDTO.getGenre());
        book.setLanguage(bookDTO.getLanguage());
        book.setPrice(bookDTO.getPrice());
        book.setDiscountPrice(bookDTO.getDiscountPrice());
        book.setPages(bookDTO. getPages());
        book.setPublicationDate(bookDTO. getPublicationDate());
        book.setQuantityInStock(bookDTO.getQuantityInStock());
        book.setInStock(bookDTO.getQuantityInStock() > 0);
        book.setFeatured(bookDTO.isFeatured());
        book.setActive(bookDTO.isActive());
        book.setUpdatedAt(LocalDateTime.now());

        return bookRepository. save(book);
    }

    /**
     * Удалить книгу (только админ)
     */
    public void deleteBook(String id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    /**
     * Увеличить счётчик просмотров
     */
    public void incrementViewCount(String id) {
        Book book = getBookById(id);
        book.setViewCount(book.getViewCount() + 1);
        bookRepository.save(book);
    }
}