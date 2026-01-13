package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.exception.CustomException;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java. util.List;

/**
 * User Service
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация нового пользователя
     */
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already registered", 409);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("Username already taken", 409);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request. getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.UserRole.USER);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * Получить пользователя по email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", 404));
    }

    /**
     * Получить пользователя по ID
     */
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", 404));
    }

    /**
     * Валидация учетных данных
     */
    public boolean validateCredentials(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user. getPassword()))
                .orElse(false);
    }

    /**
     * Обновить профиль пользователя
     */
    public User updateUserProfile(String id, User userDetails) {
        User user = getUserById(id);
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setAddress(userDetails.getAddress());
        user.setCity(userDetails.getCity());
        user.setZipCode(userDetails.getZipCode());
        user.setCountry(userDetails.getCountry());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Добавить книгу в wishlist
     */
    public User addToFavorites(String userId, String bookId) {
        User user = getUserById(userId);
        if (user.getFavoriteBooks() == null) {
            user. setFavoriteBooks(new java.util.ArrayList<>());
        }
        if (! user.getFavoriteBooks().contains(bookId)) {
            user.getFavoriteBooks().add(bookId);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        return user;
    }

    /**
     * Удалить книгу из wishlist
     */
    public User removeFromFavorites(String userId, String bookId) {
        User user = getUserById(userId);
        if (user.getFavoriteBooks() != null) {
            user.getFavoriteBooks().remove(bookId);
            user.setUpdatedAt(LocalDateTime. now());
            userRepository.save(user);
        }
        return user;
    }

    /**
     * Получить все пользователей (только для админа)
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}