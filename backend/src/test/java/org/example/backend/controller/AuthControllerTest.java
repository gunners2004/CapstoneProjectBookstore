package org.example.backend.controller;
import com.fasterxml.jackson.databind. ObjectMapper;
import org.example.backend.dto.LoginRequest;
import org.example.backend.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit. jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.boot.test.autoconfigure. web.servlet.AutoConfigureMockMvc;
import org. springframework.boot.test.context. SpringBootTest;
import org. springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter. Container;
import org.testcontainers.junit.jupiter. Testcontainers;

import static org.springframework.test.web. servlet.request.MockMvcRequestBuilders.*;
import static org. springframework.test.web.servlet. result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration Tests for Auth Controller
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DisplayName("Auth Controller Integration Tests")
class AuthControllerTest {

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

    @Test
    @DisplayName("Should register new user successfully")
    void testRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@bookstore.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper. writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Should reject duplicate email")
    void testRegisterWithDuplicateEmail() throws Exception {
        // Первая регистрация
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("user1");
        request1.setEmail("duplicate@bookstore.com");
        request1.setPassword("password123");
        request1.setFirstName("Test");
        request1.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        // Вторая регистрация с тем же email
        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("user2");
        request2.setEmail("duplicate@bookstore.com");
        request2.setPassword("password456");
        request2.setFirstName("Another");
        request2.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(containsString("already registered")));
    }

    @Test
    @DisplayName("Should login successfully")
    void testLogin() throws Exception {
        // Сначала регистрируемся
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("loginuser");
        registerRequest.setEmail("login@bookstore.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("Login");
        registerRequest.setLastName("User");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Теперь логинимся
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("login@bookstore.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("loginuser"));
    }

    @Test
    @DisplayName("Should reject invalid credentials")
    void testLoginWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@bookstore.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
