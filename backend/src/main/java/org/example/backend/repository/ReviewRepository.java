package org.example.backend.repository;


import org.example.backend.model.Review;
import org. springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Review Repository
 */
@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    // Получить отзывы для книги
    Page<Review> findByBookId(String bookId, Pageable pageable);

    // Получить отзывы пользователя
    Page<Review> findByUserId(String userId, Pageable pageable);

    // Средняя оценка для книги
    double findAverageRatingByBookId(String bookId);
}
