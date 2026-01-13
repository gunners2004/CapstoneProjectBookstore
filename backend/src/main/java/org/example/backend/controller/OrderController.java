package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Order;
import org.example.backend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework. data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework. http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Order Controller (без JWT - используется session)
 * Endpoints для:
 * - Создания заказов
 * - Получения истории заказов
 * - Отслеживания заказов
 * - Управления статусом заказов (админ)
 */
@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Получить текущего пользователя
     */
    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new CustomException("User not authenticated", 401);
        }
        return auth.getName();
    }

    /**
     * POST /api/orders - Создать новый заказ
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            String userId = getCurrentUserId();
            Order order = orderService.createOrder(userId, orderDTO);

            Map<String, Object> response = new HashMap<>();
            response. put("message", "Order created successfully");
            response.put("order", order);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e. getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/orders - Получить мои заказы
     */
    @GetMapping
    public ResponseEntity<?> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String userId = getCurrentUserId();
            Pageable pageable = PageRequest. of(page, size);
            Page<Order> orders = orderService.getUserOrders(userId, pageable);

            return ResponseEntity. ok(orders);
        } catch (CustomException e) {
            return ResponseEntity. status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/orders/{id} - Получить заказ по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        try {
            String userId = getCurrentUserId();
            Order order = orderService.getOrderById(id);

            // Проверяем права доступа
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (! order.getUserId().equals(userId) &&
                    !auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "Access denied"));
            }

            return ResponseEntity.ok(order);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/orders/{id}/cancel - Отменить заказ
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        try {
            String userId = getCurrentUserId();
            Order order = orderService.cancelOrder(id, userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order cancelled successfully");
            response.put("order", order);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/orders/{id}/status - Обновить статус заказа (только админ)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<? > updateOrderStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity. badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            Order order = orderService.updateOrderStatus(id, status);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Order status updated");
            response.put("order", order);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e. getMessage()));
        }
    }
}