package org.example.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data. annotation.Id;
import org. springframework.data.mongodb.core. mapping.Document;
import java. math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cart Entity - Корзина покупок пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private String userId;
    private List<CartItem> items;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void recalculateTotal() {
        this.subtotal = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.tax = this.subtotal.multiply(BigDecimal.valueOf(0.1)); // 10% налог
        this.total = this.subtotal.add(this. tax);
    }
}
