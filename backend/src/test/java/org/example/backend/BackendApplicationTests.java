package org.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org. springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Integration Tests for BookStore
 * Uses Testcontainers for MongoDB
 */
@SpringBootTest
@Testcontainers
class BackendApplicationTests {

    @Container
    static MongoDBContainer mongoDatabase = new MongoDBContainer("mongo:6. 0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDatabase:: getReplicaSetUrl);
    }

    @Test
    void contextLoads() {
        // Проверяем что приложение загружается без ошибок
    }
}
