# Use Eclipse Temurin 17 as the base image (recommended for Spring Boot 3.x)
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Install necessary packages and create a non-root user for security
RUN apk add --no-cache curl \
    && addgroup -g 1000 spring \
    && adduser -u 1000 -G spring -s /bin/sh -D spring

# Copy the Maven wrapper and pom.xml (for dependency caching)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Change ownership of the app directory to the spring user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose the port (Render will set the PORT environment variable)
EXPOSE $PORT

# Set JVM options for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check (optional but recommended for Render)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:$PORT/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]