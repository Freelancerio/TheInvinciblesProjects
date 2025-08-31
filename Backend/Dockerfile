# Use Java 17 base image
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Add execute permission to mvnw
RUN chmod +x ./mvnw

# Package the app
RUN ./mvnw clean package -DskipTests

# -------------------------
# Final runtime image
# -------------------------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Don't expose a specific port - Render will set the PORT env var
# EXPOSE directive is not needed for Render

# Run the app with server address binding to all interfaces
CMD ["java", "-Dserver.address=0.0.0.0", "-jar", "app.jar"]