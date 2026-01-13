package org.example.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для элемента заказа
 *
 * Представляет книгу в составе заказа с информацией о:
 * - ID и названии книги
 * - Авторе и цене при покупке
 * - Количестве заказанных копий
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    // Информация о книге
    private String bookId;
    private String bookTitle;
    private String author;
    private String isbn;
    private String imageUrl;

    // Информация о покупке
    private int quantity;
    private BigDecimal price; // Цена в момент покупки
    private BigDecimal subtotal; // quantity * price

    /**
     * Получить сумму (количество * цена)
     */
    public BigDecimal getSubtotal() {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Проверить что данные валидны
     */
    public boolean isValid() {
        return bookId != null && !bookId.isEmpty() &&
                quantity > 0 &&
                price != null && price.compareTo(BigDecimal. ZERO) >= 0;
    }
}