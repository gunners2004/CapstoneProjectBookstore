package org.example.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для книги
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Author is required")
    private String author;

    private String publisher;
    private String isbn;
    private String genre;
    private String language;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    private BigDecimal discountPrice;

    private int pages;
    private String publicationDate;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantityInStock;

    private String imageUrl;
    private boolean featured;
    private boolean active;
}