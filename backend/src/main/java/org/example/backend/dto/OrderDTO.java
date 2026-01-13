package org.example.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для заказа
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String id;
    private String orderNumber;

    private List<OrderItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    @NotBlank(message = "Country is required")
    private String country;

    private String billingAddress;
    private String paymentMethod;
    private String status;
}