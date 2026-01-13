package org.example.backend.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * CartItem - Элемент в корзине
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String bookId;
    private String bookTitle;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}