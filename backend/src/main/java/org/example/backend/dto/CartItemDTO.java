package org.example.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для элемента корзины
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    @NotBlank(message = "Book ID is required")
    private String bookId;

    private String bookTitle;
    private String imageUrl;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Maximum quantity is 100")
    private int quantity;

    private BigDecimal price;
}
