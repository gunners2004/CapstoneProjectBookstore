package org.example.backend.repository;

import org.example.backend.model.Book;
import org. springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Book Repository
 */
@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    // Поиск по названию (содержит текст, без учета регистра)
    Page<Book> findByTitleIgnoreCaseContaining(String title, Pageable pageable);

    // Поиск по автору
    Page<Book> findByAuthorIgnoreCase(String author, Pageable pageable);

    // Поиск по жанру
    Page<Book> findByGenreIgnoreCase(String genre, Pageable pageable);

    // Поиск по цене
    Page<Book> findByPriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice, Pageable pageable);

    // Избранные книги
    Page<Book> findByFeaturedTrue(Pageable pageable);

    // В наличии
    Page<Book> findByInStockTrue(Pageable pageable);

    // Полнотекстовый поиск
    @Query("{ $text: { $search: ?0 } }")
    Page<Book> searchBooks(String searchTerm, Pageable pageable);

    // По ISBN
    Book findByIsbn(String isbn);
}