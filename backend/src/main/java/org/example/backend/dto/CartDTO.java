package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для корзины
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private String id;
    private List<CartItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
}