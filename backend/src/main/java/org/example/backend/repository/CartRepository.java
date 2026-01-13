package org.example.backend.repository;

import org.example.backend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Cart Repository
 */
@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}