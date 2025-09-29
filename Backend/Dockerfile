# =========================
# Stage 1: Build & Test
# =========================
FROM maven:3.9.6-openjdk-17-slim AS build

# Install Docker CLI for Testcontainers (optional for local dev/test)
RUN apt-get update && \
    apt-get install -y docker.io curl && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Maven config and download dependencies
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Run tests (can be skipped in CI/CD if needed by passing --target runtime)
ARG SKIP_TESTS=false
RUN if [ "$SKIP_TESTS" = "true" ]; then \
      ./mvnw clean package -DskipTests -B; \
    else \
      ./mvnw clean verify -Dspring.profiles.active=test -B; \
    fi

# =========================
# Stage 2: Runtime Image
# =========================
FROM eclipse-temurin:17-jdk-alpine AS runtime

WORKDIR /app

# Add non-root user for security
RUN addgroup -g 1000 spring && \
    adduser -u 1000 -G spring -s /bin/sh -D spring

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership
RUN chown spring:spring /app/app.jar
USER spring

# Render exposes port dynamically
EXPOSE $PORT

# JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:$PORT/actuator/health || exit 1

# Run app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
