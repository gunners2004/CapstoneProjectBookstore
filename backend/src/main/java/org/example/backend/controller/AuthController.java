package org.example.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.backend.dto.LoginRequest;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.exception.CustomException;
import org.example.backend.model.User;
import org.example.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org. springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Auth Controller - Session-based authentication
 *
 * Endpoints для:
 * - Регистрации пользователей
 * - Логина (создание сессии)
 * - Логаута (удаление сессии)
 * - Проверки статуса аутентификации
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private static final String SECURITY_CONTEXT_REPOSITORY_ATTR =
            HttpSessionSecurityContextRepository. SPRING_SECURITY_CONTEXT_KEY;

    /**
     * POST /api/auth/register - Регистрация нового пользователя
     *
     * После регистрации пользователь автоматически логируется
     * и создаёется сессия
     *
     * @param request Данные для регистрации
     * @param httpSession Сессия HTTP
     * @return Информация о пользователе и ID сессии
     */
    @PostMapping("/register")
    public ResponseEntity<? > register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpServletRequest,
            HttpSession httpSession) {
        try {
            // Валидируем входные данные
            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                        .body(Map. of("error", "Password must be at least 6 characters"));
            }

            // Регистрируем пользователя
            User user = userService.registerUser(request);

            // Аутентифицируем пользователя (создаём сессию)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user. getEmail(),
                    request.getPassword()
            );

            authentication = authenticationManager.authenticate(authentication);

            // Сохраняем ��утентификацию в SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // Сохраняем SecurityContext в сессию
            // Spring Session автоматически сохранит это в MongoDB
            httpSession.setAttribute(SECURITY_CONTEXT_REPOSITORY_ATTR, securityContext);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered and logged in successfully");
            response. put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().name());
            response.put("sessionId", httpSession.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map. of("error", "Registration failed:  " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login - Логин пользователя
     *
     * Создаёт сессию и отправляет JSESSIONID в cookie
     *
     * @param request Email и пароль
     * @param httpSession Сессия HTTP
     * @return Информация о пользователе
     */
    @PostMapping("/login")
    public ResponseEntity<? > login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest,
            HttpSession httpSession) {
        try {
            // Валидируем входные данные
            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                return ResponseEntity. badRequest()
                        .body(Map.of("error", "Email is required"));
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Password is required"));
            }

            // Аутентифицируем пользователя
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );

            try {
                authentication = authenticationManager.authenticate(authentication);
            } catch (BadCredentialsException e) {
                return ResponseEntity. status(401)
                        .body(Map.of("error", "Invalid email or password"));
            }

            // Сохраняем аутентификацию в SecurityContext
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // Сохраняем SecurityContext в сессию
            // Spring Session автоматически сохранит в MongoDB
            httpSession.setAttribute(SECURITY_CONTEXT_REPOSITORY_ATTR, securityContext);

            // Получаем информацию о пользователе
            User user = userService.getUserByEmail(request.getEmail());

            // Обновляем время последнего логина
            user.setLastLogin(java.time.LocalDateTime.now());
            userService.updateUserProfile(user. getId(), user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("role", user.getRole().name());
            response. put("sessionId", httpSession.getId());

            return ResponseEntity.ok(response);

        } catch (CustomException e) {
            return ResponseEntity. status(e.getStatusCode())
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map. of("error", "Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * POST /api/auth/logout - Логаут пользователя
     *
     * Удаляет сессию из MongoDB и очищает SecurityContext
     *
     * @param httpSession Сессия HTTP
     * @return Сообщение об успеше
     */
    @PostMapping("/logout")
    public ResponseEntity<? > logout(
            HttpServletRequest httpServletRequest,
            HttpSession httpSession) {
        try {
            // Получаем текущую аутентификацию перед очисткой
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // Очищаем SecurityContext
            SecurityContextHolder.clearContext();

            // Инвалидируем сессию (удаляет из MongoDB)
            httpSession.invalidate();

            return ResponseEntity.ok(Map.of(
                    "message", "Logout successful",
                    "user", auth != null ? auth.getName() : "unknown"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(Map. of("error", "Logout failed: " + e.getMessage()));
        }
    }

    /**
     * GET /api/auth/me - Получить информацию о текущем пользователе
     *
     * Проверяет сессию и возвращает информацию если авторизован
     *
     * @return Информация о текущем пользователе
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Проверяем авторизован ли пользователь
            if (authentication == null || ! authentication.isAuthenticated() ||
                    "anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity. status(401)
                        . body(Map.of("error", "User not authenticated"));
            }

            // Получаем email из аутентификации
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user. getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("role", user.getRole().name());
            response. put("phoneNumber", user.getPhoneNumber());
            response.put("address", user.getAddress());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(Map. of("error", "User not authenticated:  " + e.getMessage()));
        }
    }

    /**
     * GET /api/auth/check - Проверить авторизован ли пользователь
     *
     * Простая проверка статуса без возврата данных пользователя
     *
     * @return { authenticated: boolean }
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication() {
        try {
            Authentication authentication = SecurityContextHolder. getContext().getAuthentication();

            boolean isAuthenticated = authentication != null &&
                    authentication.isAuthenticated() &&
                    !"anonymousUser".equals(authentication.getPrincipal());

            return ResponseEntity.ok(Map.of(
                    "authenticated", isAuthenticated,
                    "user", isAuthenticated ? authentication.getName() : null
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }

    /**
     * GET /api/auth/expired - Сессия истекла
     *
     * Возвращается когда сессия идентифицирована как истёкшая
     *
     * @return Сообщение об истечении сессии
     */
    @GetMapping("/expired")
    public ResponseEntity<?> sessionExpired() {
        return ResponseEntity.status(401)
                .body(Map.of("error", "Session expired.  Please login again."));
    }

    /**
     * GET /api/auth/logout-success - Успешный логаут
     *
     * @return Сообщение об успехе
     */
    @GetMapping("/logout-success")
    public ResponseEntity<?> logoutSuccess() {
        return ResponseEntity. ok(Map.of("message", "You have been logged out successfully"));
    }

    /**
     * GET /api/auth/login-failure - Ошибка логина
     *
     * @return Сообщение об ошибке
     */
    @GetMapping("/login-failure")
    public ResponseEntity<?> loginFailure() {
        return ResponseEntity.status(401)
                .body(Map.of("error", "Authentication failed"));
    }
}