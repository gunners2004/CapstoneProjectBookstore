package org.example.backend.repository;

import org.example.backend.model.Order;
import org. springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Order Repository
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // Получить заказы пользователя
    Page<Order> findByUserId(String userId, Pageable pageable);

    // По номеру заказа
    Optional<Order> findByOrderNumber(String orderNumber);

    // По номеру отслеживания
    Optional<Order> findByTrackingNumber(String trackingNumber);
}
