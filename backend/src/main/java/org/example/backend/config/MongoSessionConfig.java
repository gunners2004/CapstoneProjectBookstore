package org.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

/**
 * MongoDB Session Configuration
 *
 * Конфигурирует хранение сессий в MongoDB:
 * - Время жизни сессии: 30 минут
 * - Коллекция: spring_session
 */
@Configuration
@EnableMongoHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 минут
public class MongoSessionConfig {
    // Конфигурация загружается автоматически из spring-session-data-mongodb
}