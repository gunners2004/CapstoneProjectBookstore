package org.example.backend.controller;

import lombok.AllArgsConstructor;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Cart;
import org.example.backend.service.CartService;
import org. springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Cart Controller (без JWT - используется session)
 *
 * Endpoints для:
 * - Получения корзины
 * - Добавления товаров
 * - Обновления количества
 * - Удаления товаров
 * - Очистки корзины
 */
@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Получить текущего пользователя из Security Context
     */
    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth. isAuthenticated()) {
            throw new CustomException("User not authenticated", 401);
        }
        return auth.getName(); // Возвращаем email
    }

    /**
     * GET /api/cart - Получить корзину пользователя
     */
    @GetMapping
    public ResponseEntity<?> getCart() {
        try {
            String userId = getCurrentUserId();
            Cart cart = cartService.getOrCreateCart(userId);
            return ResponseEntity.ok(cart);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/cart/add - Добавить товар в корзину
     */
    @PostMapping("/add")
    public ResponseEntity<? > addToCart(@RequestBody Map<String, Object> request) {
        try {
            String userId = getCurrentUserId();
            String bookId = (String) request.get("bookId");
            int quantity = ((Number) request.get("quantity")).intValue();

            if (bookId == null || bookId.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map. of("error", "Book ID is required"));
            }

            Cart cart = cartService.addToCart(userId, bookId, quantity);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item added to cart");
            response. put("cart", cart);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/cart/items/{bookId} - Обновить количество товара
     */
    @PutMapping("/items/{bookId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable String bookId,
            @RequestBody Map<String, Object> request) {
        try {
            String userId = getCurrentUserId();
            int quantity = ((Number) request.get("quantity")).intValue();

            Cart cart = cartService.updateCartItem(userId, bookId, quantity);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cart updated");
            response.put("cart", cart);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/cart/items/{bookId} - Удалить товар из корзины
     */
    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String bookId) {
        try {
            String userId = getCurrentUserId();
            Cart cart = cartService.removeFromCart(userId, bookId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item removed from cart");
            response.put("cart", cart);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/cart - Очистить корзину
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        try {
            String userId = getCurrentUserId();
            cartService.clearCart(userId);

            return ResponseEntity.ok(Map.of("message", "Cart cleared"));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/cart/count - Получить количество товаров в корзине
     */
    @GetMapping("/count")
    public ResponseEntity<?> getCartItemCount() {
        try {
            String userId = getCurrentUserId();
            int count = cartService. getCartItemCount(userId);

            return ResponseEntity.ok(Map.of("count", count));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }
}