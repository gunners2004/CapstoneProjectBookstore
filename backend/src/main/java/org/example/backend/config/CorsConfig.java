package org.example.backend.config;

import org.springframework.context. annotation.Bean;
import org. springframework.context.annotation.Configuration;
import org.springframework.web. cors.CorsConfiguration;
import org.springframework.web.cors. UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS Configuration
 *
 * Позволяет фронтенду отправлять запросы на бэкенд с другого источника
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем локальные источники
        config.addAllowedOrigin("http://localhost:5173");  // Vite dev сервер
        config.addAllowedOrigin("http://localhost:3000");  // React dev сервер
        config.addAllowedOrigin("http://localhost");       // Production
        config.addAllowedOrigin("http://localhost:80");

        // Разрешаем все HTTP методы
        config.addAllowedMethod("*");

        // Разрешаем все заголовки
        config.addAllowedHeader("*");

        // ВАЖНО: Разрешаем отправку cookies (для сессий)
        config.setAllowCredentials(true);

        // Время кэша preflight запроса
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}