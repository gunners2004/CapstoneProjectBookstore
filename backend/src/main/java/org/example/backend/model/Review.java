package org.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Review Entity - Отзыв о книге
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private String bookId;
    private String userId;
    private String username;

    private int rating; // 1-5
    private String title;
    private String comment;

    private int helpfulCount;
    private int unhelpfulCount;

    private boolean verified; // Проверено ли что пользователь купил книгу

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}