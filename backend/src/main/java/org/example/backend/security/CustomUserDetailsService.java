package org.example.backend.security;

import lombok.AllArgsConstructor;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.security.core. GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security. core.userdetails.UserDetailsService;
import org.springframework. security.core.userdetails. UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Custom User Details Service
 *
 * Загружает детали пользователя для Spring Security
 * Использует EMAIL какUsername
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает пользователя по email (используется как username)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Проверяем активен ли пользователь
        if (!user.isActive()) {
            throw new UsernameNotFoundException("User account is disabled");
        }

        // Создаём collection с ролями
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Возвращаем UserDetails для Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Email как username
                .password(user.getPassword()) // BCrypt зашифрованный пароль
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(! user.isActive())
                .build();
    }
}