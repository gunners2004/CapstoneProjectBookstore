package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dto.OrderDTO;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Book;
import org.example.backend.model.Cart;
import org.example.backend.model.Order;
import org.example.backend.model.OrderItem;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.OrderRepository;
import org.springframework. data.domain.Page;
import org.springframework.data.domain. Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java. util.ArrayList;
import java. util.List;
import java. util.UUID;

/**
 * Order Service - Сервис для работы с заказами
 *
 * Функции:
 * - Создание заказа из корзины
 * - Получение истории заказов
 * - Управление статусом заказа
 * - Отслеживание доставки
 */
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;

    /**
     * Создать заказ из корзины
     *
     * @param userId ID пользователя
     * @param orderDTO DTO с данными заказа (адрес, способ оплаты и т.д.)
     * @return Созданный заказ
     */
    public Order createOrder(String userId, OrderDTO orderDTO) {
        Cart cart = cartService.getOrCreateCart(userId);

        if (cart. getItems().isEmpty()) {
            throw new CustomException("Cart is empty", 400);
        }

        // Создаём элементы заказа из корзины
        List<OrderItem> orderItems = new ArrayList<>();
        for (var cartItem : cart.getItems()) {
            Book book = bookRepository. findById(cartItem.getBookId())
                    .orElseThrow(() -> new CustomException("Book not found", 404));

            // Проверяем наличие товара
            if (book.getQuantityInStock() < cartItem.getQuantity()) {
                throw new CustomException("Insufficient stock for:  " + book.getTitle(), 400);
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(cartItem.getBookId());
            orderItem.setBookTitle(cartItem.getBookTitle());
            orderItem.setAuthor(book.getAuthor());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            orderItems.add(orderItem);

            // Уменьшаем количество товара на складе
            book.setQuantityInStock(book.getQuantityInStock() - cartItem.getQuantity());
            book.setPurchaseCount(book.getPurchaseCount() + cartItem.getQuantity());

            // Проверяем есть ли товар
            if (book.getQuantityInStock() <= 0) {
                book.setInStock(false);
            }

            bookRepository.save(book);
        }

        // Создаём заказ
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setSubtotal(cart.getSubtotal());
        order.setTax(cart. getTax());
        order.setTotal(cart.getTotal());

        // Адрес доставки
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setCity(orderDTO.getCity());
        order.setZipCode(orderDTO.getZipCode());
        order.setCountry(orderDTO.getCountry());

        // Адрес выставления счёта (если отличается)
        if (orderDTO.getBillingAddress() != null && !orderDTO.getBillingAddress().isEmpty()) {
            order.setBillingAddress(orderDTO.getBillingAddress());
        } else {
            order.setBillingAddress(orderDTO.getShippingAddress());
        }

        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // Очищаем корзину
        cartService.clearCart(userId);

        return savedOrder;
    }

    /**
     * Получить заказы пользователя (с пагинацией)
     *
     * @param userId ID пользователя
     * @param pageable Параметры пагинации
     * @return Страница заказов
     */
    public Page<Order> getUserOrders(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    /**
     * Получить заказ по ID
     *
     * @param id ID заказа
     * @return Заказ
     */
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found", 404));
    }

    /**
     * Получить заказ по номеру заказа
     *
     * @param orderNumber Номер заказа
     * @return Заказ
     */
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new CustomException("Order not found", 404));
    }

    /**
     * Обновить статус заказа (только админ)
     *
     * @param id ID заказа
     * @param status Новый статус
     * @return Обновлённый заказ
     */
    public Order updateOrderStatus(String id, String status) {
        Order order = getOrderById(id);

        try {
            Order. OrderStatus newStatus = Order.OrderStatus.valueOf(status);
            order.setStatus(newStatus);

            // Устанавливаем дату отправки если статус = SHIPPED
            if (newStatus == Order.OrderStatus.SHIPPED) {
                order.setShippedDate(LocalDateTime.now());
            }

            // Устанавливаем дату доставки если статус = DELIVERED
            if (newStatus == Order.OrderStatus.DELIVERED) {
                order.setDeliveredDate(LocalDateTime.now());
            }

            order. setUpdatedAt(LocalDateTime. now());
            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid order status", 400);
        }
    }

    /**
     * Обновить номер отслеживания (только админ)
     *
     * @param id ID заказа
     * @param trackingNumber Номер отслеживания
     * @return Обновлённый заказ
     */
    public Order updateTrackingNumber(String id, String trackingNumber) {
        Order order = getOrderById(id);
        order.setTrackingNumber(trackingNumber);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * Отменить заказ
     *
     * @param id ID заказа
     * @param userId ID пользователя (для проверки прав)
     * @return Обновлённый заказ
     */
    public Order cancelOrder(String id, String userId) {
        Order order = getOrderById(id);

        // Проверяем что это заказ пользователя
        if (!order.getUserId().equals(userId)) {
            throw new CustomException("You can only cancel your own orders", 403);
        }

        // Можно отменить только ещё не отправленные заказы
        if (order.getStatus() != Order.OrderStatus.PENDING &&
                order.getStatus() != Order.OrderStatus.PROCESSING) {
            throw new CustomException("Cannot cancel orders that are already shipped", 400);
        }

        // Возвращаем товары на склад
        for (OrderItem item : order.getItems()) {
            Book book = bookRepository.findById(item.getBookId()).orElse(null);
            if (book != null) {
                book.setQuantityInStock(book.getQuantityInStock() + item.getQuantity());
                book.setPurchaseCount(Math.max(0, book.getPurchaseCount() - item.getQuantity()));

                if (book.getQuantityInStock() > 0) {
                    book.setInStock(true);
                }

                bookRepository.save(book);
            }
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * Получить заказ по номеру отслеживания
     *
     * @param trackingNumber Номер отслеживания
     * @return Заказ
     */
    public Order getOrderByTrackingNumber(String trackingNumber) {
        return orderRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new CustomException("Order not found", 404));
    }

    /**
     * Генерируем уникальный номер заказа
     * Формат: ORD-YYYYMMDDHHMMSS-XXXX
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}