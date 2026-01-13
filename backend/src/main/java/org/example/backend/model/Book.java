package org.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data. annotation.Id;
import org. springframework.data.mongodb.core. mapping.Document;
import org. springframework.data.mongodb.core. index.Indexed;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Book Entity - Представляет книгу в магазине
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    @Indexed
    private String title;

    private String description;
    private String author;
    private String publisher;
    private String isbn;

    @Indexed
    private String genre;
    private String language;

    private BigDecimal price;
    private BigDecimal discountPrice;

    private int pages;
    private String publicationDate;

    // Stock management
    private int quantityInStock;
    private boolean inStock;

    // Image storage
    private String imageUrl;
    private String imagePath;

    // Ratings
    private double averageRating;
    private int ratingCount;

    // Statistics
    private int viewCount;
    private int purchaseCount;

    private boolean featured;
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getDisplayPrice() {
        return discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0
                ? discountPrice.toPlainString()
                : price.toPlainString();
    }
}