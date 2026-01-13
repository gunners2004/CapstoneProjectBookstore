package org.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * OrderItem - Элемент заказа
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String bookId;
    private String bookTitle;
    private String author;
    private int quantity;
    private BigDecimal price;

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}