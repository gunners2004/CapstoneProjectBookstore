package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dto.ReviewDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Book;
import org.example.backend.model.Review;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain. Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Review Service - Сервис для работы с отзывами
 *
 * Функции:
 * - Добавление отзывов о книгах
 * - Получение отзывов для книги
 * - Обновление и удаление отзывов
 * - Расчёт среднего рейтинга книги
 */
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    /**
     * Добавить отзыв о книге
     *
     * @param userId ID пользователя
     * @param username Имя пользователя
     * @param reviewDTO DTO с данными отзыва
     * @return Созданный отзыв
     */
    public Review addReview(String userId, String username, ReviewDTO reviewDTO) {
        // Проверяем существует ли книга
        Book book = bookRepository.findById(reviewDTO.getBookId())
                .orElseThrow(() -> new CustomException("Book not found", 404));

        Review review = new Review();
        review.setBookId(reviewDTO.getBookId());
        review.setUserId(userId);
        review.setUsername(username);
        review.setRating(reviewDTO.getRating());
        review.setTitle(reviewDTO.getTitle());
        review.setComment(reviewDTO.getComment());
        review.setVerified(reviewDTO.isVerified());
        review.setHelpfulCount(0);
        review.setUnhelpfulCount(0);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        // Обновляем средний рейтинг книги
        updateBookRating(reviewDTO.getBookId());

        return savedReview;
    }

    /**
     * Получить отзывы для книги (с пагинацией)
     *
     * @param bookId ID книги
     * @param pageable Параметры пагинации
     * @return Страница отзывов
     */
    public Page<Review> getReviewsByBook(String bookId, Pageable pageable) {
        // Проверяем существует ли книга
        bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("Book not found", 404));

        return reviewRepository.findByBookId(bookId, pageable);
    }

    /**
     * Получить отзывы пользователя (с пагинацией)
     *
     * @param userId ID пользователя
     * @param pageable Параметры пагинации
     * @return Страница отзывов
     */
    public Page<Review> getUserReviews(String userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable);
    }

    /**
     * Получить отзыв по ID
     *
     * @param id ID отзыва
     * @return Отзыв
     */
    public Review getReviewById(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException("Review not found", 404));
    }

    /**
     * Обновить отзыв
     *
     * @param id ID отзыва
     * @param userId ID пользователя (для проверки прав)
     * @param reviewDTO DTO с новыми данными
     * @return Обновлённый отзыв
     */
    public Review updateReview(String id, String userId, ReviewDTO reviewDTO) {
        Review review = getReviewById(id);

        // Проверяем что это отзыв пользователя
        if (!  review.getUserId().equals(userId)) {
            throw new CustomException("You can only edit your own reviews", 403);
        }

        review.setRating(reviewDTO.getRating());
        review.setTitle(reviewDTO.getTitle());
        review.setComment(reviewDTO.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository. save(review);

        // Обновляем средний рейтинг книги
        updateBookRating(review.getBookId());

        return updatedReview;
    }

    /**
     * Удалить отзыв
     *
     * @param id ID отзыва
     * @param userId ID пользователя (для проверки прав)
     */
    public void deleteReview(String id, String userId) {
        Review review = getReviewById(id);

        // Проверяем что это отзыв пользователя
        if (! review.getUserId().equals(userId)) {
            throw new CustomException("You can only delete your own reviews", 403);
        }

        String bookId = review.getBookId();
        reviewRepository.delete(review);

        // Обновляем средний рейтинг книги
        updateBookRating(bookId);
    }

    /**
     * Пометить отзыв как полезный
     *
     * @param id ID отзыва
     */
    public Review markAsHelpful(String id) {
        Review review = getReviewById(id);
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        return reviewRepository.save(review);
    }

    /**
     * Пометить отзыв как неполезный
     *
     * @param id ID отзыва
     */
    public Review markAsUnhelpful(String id) {
        Review review = getReviewById(id);
        review.setUnhelpfulCount(review.getUnhelpfulCount() + 1);
        return reviewRepository. save(review);
    }

    /**
     * Обновить средний рейтинг книги
     * (Вспомогательный метод)
     */
    private void updateBookRating(String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("Book not found", 404));

        Page<Review> reviews = reviewRepository. findByBookId(bookId,
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE));

        if (reviews.getTotalElements() > 0) {
            double averageRating = reviews.getContent().stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0);

            book.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
            book.setRatingCount((int) reviews.getTotalElements());
            bookRepository.save(book);
        }
    }
}
