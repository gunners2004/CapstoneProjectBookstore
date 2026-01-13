package org.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java. util.List;

/**
 * User Entity - Представляет пользователя книжного магазина
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    private String firstName;
    private String lastName;
    private String password; // BCrypt encoded
    private String phoneNumber;
    private String address;
    private String city;
    private String zipCode;
    private String country;

    private UserRole role;
    private boolean active;
    private boolean emailVerified;

    // Wishlist
    private List<String> favoriteBooks; // IDs книг

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;

    public enum UserRole {
        USER, ADMIN
    }
}
