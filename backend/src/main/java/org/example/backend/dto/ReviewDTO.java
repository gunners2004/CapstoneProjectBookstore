package org.example.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для отзыва
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private String id;

    @NotBlank(message = "Book ID is required")
    private String bookId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Title is required")
    private String title;

    private String comment;
    private boolean verified;
}
