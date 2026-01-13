package org.example.backend.controller;


import com. fasterxml.jackson.databind. ObjectMapper;
import org.example.backend.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit. jupiter.api.DisplayName;
import org.junit. jupiter.api.Test;
import org.springframework.beans.factory. annotation.Autowired;
import org.springframework.boot.test. autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers. junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web. servlet.request.MockMvcRequestBuilders.*;
import static org. springframework.test.web.servlet. result.MockMvcResultMatchers.*;


/**
 * Integration Tests for Book Controller
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("Book Controller Integration Tests")
class BookControllerTest {

    @Container
    static MongoDBContainer mongoDatabase = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDatabase::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookDTO testBook;

    @BeforeEach
    void setUp() {
        testBook = new BookDTO();
        testBook.setTitle("Spring Boot in Action");
        testBook.setAuthor("Craig Walls");
        testBook.setPublisher("Manning");
        testBook.setIsbn("978-1617292545");
        testBook.setGenre("Technology");
        testBook.setLanguage("English");
        testBook.setPrice(BigDecimal. valueOf(49.99));
        testBook.setPages(520);
        testBook.setQuantityInStock(100);
        testBook.setFeatured(true);
        testBook.setActive(true);
    }

    @Test
    @DisplayName("Should get all books with pagination")
    void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books? page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should search books by title")
    void testSearchByTitle() throws Exception {
        // Сначала создаём книгу (требует админ токена)
        mockMvc.perform(get("/api/books/search?title=Spring&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should get featured books")
    void testGetFeaturedBooks() throws Exception {
        mockMvc.perform(get("/api/books/featured?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should return 404 for non-existent book")
    void testGetNonExistentBook() throws Exception {
        mockMvc.perform(get("/api/books/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
