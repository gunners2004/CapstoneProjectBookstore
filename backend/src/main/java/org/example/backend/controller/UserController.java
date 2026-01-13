package org.example.backend.controller;

import lombok.AllArgsConstructor;
import org.example.backend.exception.CustomException;
import org.example.backend.model.User;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework. security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework. web.bind.annotation.*;
import java.util.Map;

/**
 * User Controller
 *
 * Endpoints для:
 * - Получения профиля пользователя
 * - Обновления профиля
 * - Управления wishlist
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users/profile - Получить профиль текущего пользователя
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication. isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/users/profile - Обновить профиль
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User userDetails) {
        try {
            Authentication authentication = SecurityContextHolder. getContext().getAuthentication();
            if (authentication == null || ! authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String email = authentication.getName();
            User user = userService. getUserByEmail(email);

            User updatedUser = userService.updateUserProfile(user. getId(), userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map. of("error", e.getMessage()));
        }
    }

    /**
     * POST /api/users/favorites/{bookId} - Добавить в избранное
     */
    @PostMapping("/favorites/{bookId}")
    public ResponseEntity<?> addToFavorites(@PathVariable String bookId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication. isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            User updatedUser = userService.addToFavorites(user.getId(), bookId);

            return ResponseEntity.ok(Map.of(
                    "message", "Book added to favorites",
                    "favorites", updatedUser. getFavoriteBooks()
            ));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/users/favorites/{bookId} - Удалить из избранного
     */
    @DeleteMapping("/favorites/{bookId}")
    public ResponseEntity<?> removeFromFavorites(@PathVariable String bookId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            User updatedUser = userService.removeFromFavorites(user.getId(), bookId);

            return ResponseEntity.ok(Map. of(
                    "message", "Book removed from favorites",
                    "favorites", updatedUser.getFavoriteBooks()
            ));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/users/favorites - Получить список избранных книг
     */
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(Map.of("error", "User not authenticated"));
            }

            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            return ResponseEntity.ok(Map.of(
                    "favorites", user.getFavoriteBooks() != null ? user.getFavoriteBooks() : java.util.List.of()
            ));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        }
    }
}