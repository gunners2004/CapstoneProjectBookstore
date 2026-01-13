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
 * Order Entity - Заказ книг
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;
    private String orderNumber;

    private List<OrderItem> items;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    // Shipping Info
    private String shippingAddress;
    private String city;
    private String zipCode;
    private String country;

    // Billing
    private String billingAddress;
    private String paymentMethod;

    private OrderStatus status;

    // Tracking
    private String trackingNumber;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
}
