package org.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework. web.bind.annotation.ExceptionHandler;
import org.springframework. web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler
 *
 * Обрабатывает все исключения приложения
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException. class)
    public ResponseEntity<? > handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error. getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map. of("error", "Internal server error:  " + e.getMessage()));
    }
}
