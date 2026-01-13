package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.BookDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Book;
import org.example.backend.service.BookService;
import org.springframework.data. domain.Page;
import org. springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework. http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Book Controller (публичные эндпоинты доступны всем)
 *
 * Endpoints для:
 * - Получения списка книг
 * - Поиска книг
 * - Просмотра информации о книге
 * - Добавления/редактирования книг (только админ)
 */
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * GET /api/books - Получить все книги (с пагинацией)
     */
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest. of(page, size);
            Page<Book> books = bookService.getAllBooks(pageable);

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/books/search - Поиск книг
     */
    @GetMapping("/search")
    public ResponseEntity<? > searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> results;

            if (title != null && !title.isEmpty()) {
                results = bookService.searchByTitle(title, pageable);
            } else if (author != null && !author.isEmpty()) {
                results = bookService.searchByAuthor(author, pageable);
            } else if (genre != null && !genre.isEmpty()) {
                results = bookService. searchByGenre(genre, pageable);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please provide search criteria"));
            }

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/books/featured - Избранные книги
     */
    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> books = bookService.getFeaturedBooks(pageable);

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", e. getMessage()));
        }
    }

    /**
     * GET /api/books/{id} - Получить информацию о книге
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable String id) {
        try {
            Book book = bookService.getBookById(id);
            // Увеличиваем счётчик просмотров
            bookService. incrementViewCount(id);
            return ResponseEntity.ok(book);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/books - Создать новую книгу (только админ)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/books/{id} - Обновить книгу (только админ)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(
            @PathVariable String id,
            @Valid @RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(book);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e. getMessage()));
        }
    }

    /**
     * DELETE /api/books/{id} - Удалить книгу (только админ)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok(Map.of("message", "Book deleted successfully"));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }
}