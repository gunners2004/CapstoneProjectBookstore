# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Banking App Team"

WORKDIR /app

# Копируем Maven файлы
COPY pom. xml.
COPY . mvn

# Загружаем зависимости
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src src

# Собираем приложение
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
LABEL maintainer="Banking App Team"

WORKDIR /app

# Копируем JAR из build stage
COPY --from=builder /app/target/*. jar app.jar/

# Создаем пользователя для безопасности
RUN useradd -m -u 1000 appuser && \
    chown -R appuser:appuser /app

USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080