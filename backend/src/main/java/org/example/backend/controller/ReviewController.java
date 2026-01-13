package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.ReviewDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Review;
import org.example.backend.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data. domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org. springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Review Controller (без JWT - используется session)
 *
 * Endpoints для:
 * - Добавления отзывов о книгах
 * - Получения отзывов
 * - Обновления и удаления отзывов
 * - Оценки полезности отзывов
 */
@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * POST /api/reviews - Добавить отзыв о книге
     */
    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        try {
            // Получаем текущего пользователя из Security Context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth. isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String userId = auth.getName();
            String username = auth.getPrincipal().toString();

            Review review = reviewService. addReview(userId, username, reviewDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/reviews/book/{bookId} - Получить отзывы о книге
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<? > getBookReviews(
            @PathVariable String bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest. of(page, size);
            Page<Review> reviews = reviewService.getReviewsByBook(bookId, pageable);

            return ResponseEntity.ok(reviews);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e. getMessage()));
        }
    }

    /**
     * GET /api/reviews/user - Получить мои отзывы
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUserReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth. isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String userId = auth.getName();

            Pageable pageable = PageRequest. of(page, size);
            Page<Review> reviews = reviewService.getUserReviews(userId, pageable);

            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/reviews/{id} - Обновить отзыв
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewDTO reviewDTO) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth. isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String userId = auth.getName();

            Review review = reviewService.updateReview(id, userId, reviewDTO);
            return ResponseEntity.ok(review);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/reviews/{id} - Удалить отзыв
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map. of("error", "User not authenticated"));
            }

            String userId = auth.getName();

            reviewService.deleteReview(id, userId);
            return ResponseEntity.ok(Map. of("message", "Review deleted successfully"));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }
}