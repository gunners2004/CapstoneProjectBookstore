package org.example.backend.config;
import lombok.AllArgsConstructor;
import org.example.backend.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config. annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework. security.config.annotation.method. configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework. security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto. password.PasswordEncoder;
import org.springframework.security.web. SecurityFilterChain;

/**
 * Spring Security Configuration - Минимальный рабочий вариант
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                . csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**", "/api/books/**", "/api/reviews/book/**").permitAll()
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/api/auth/logout-success")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth. userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}