# Smartbet

[![CI/CD Pipeline](https://github.com/Freelancerio/TheInvinciblesProjects/actions/workflows/ci.yml/badge.svg)](https://github.com/Freelancerio/TheInvinciblesProjects/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/Freelancerio/TheInvinciblesProjects/branch/main/graph/badge.svg)](https://codecov.io/gh/Freelancerio/TheInvinciblesProjects)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Freelancerio_TheInvinciblesProjects&metric=alert_status)](https://sonarcloud.io/dashboard?id=Freelancerio_TheInvinciblesProjects)
[![Java Version](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green.svg)](https://spring.io/projects/spring-boot)

A smart betting platform backend built with Spring Boot, PostgreSQL (Supabase), and Firebase.

## Features

- **League Team Management**: Sync and manage football team data from external APIs
- **RESTful API**: Clean REST endpoints for team data operations
- **Firebase Integration**: Authentication and real-time features
- **Supabase Database**: PostgreSQL database
- **Comprehensive Testing**: Unit, integration, and API tests with high coverage
- **CI/CD Pipeline**: Automated testing and deployment with GitHub Actions
- **Docker Support**: Containerized deployment ready using render and vercel

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Database**: PostgreSQL (Supabase)
- **Authentication**: Firebase Admin SDK
- **Testing**: JUnit 5, Mockito, Spring Boot Test, Testcontainers
- **Build**: Maven
- **Containerization**: Docker
- **CI/CD**: GitHub Actions
- Java 17 or higher
- Maven 3.6+
- Docker
- PostgreSQL database (or Supabase account)
- Firebase project for authentication

## Setting up

### 1. Clone the repository
```bash
git clone https://github.com/Freelancerio/smartbet.git
cd smartbet
```

### 2. Configure the application
```bash
# Copy and update application.properties
cp src/main/resources/application.properties.example src/main/resources/application.properties

# Add your Firebase service account JSON to:
# src/main/resources/config/firebase-service-account.json
```

### 3. Run the application
```bash
# Using Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar

# Using Docker
docker build -t smartbet-backend .
docker run -p 8080:8080 smartbet-backend
```

##  Testing

This project includes comprehensive testing with high coverage:

### Run all tests
```bash
mvn test
```

### Generate coverage report
```bash
mvn jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`

### Test Categories

- **Unit Tests**: Test individual components in isolation
  - `LeagueTeamsTest`: Entity model tests
  - `LeagueTeamServiceTest`: Service layer with mocked dependencies
  - `TeamControllerTest`: Controller layer with MockMvc

- **Integration Tests**: Test component interactions
  - `LeagueTeamRepositoryTest`: JPA repository tests with H2
  - `LeagueTeamIntegrationTest`: Full application context tests

- **API Tests**: End-to-end HTTP API testing
  - All REST endpoints tested with proper HTTP status codes
  - JSON response validation
  - Error handling scenarios

### Coverage Goals
- **Overall Coverage**: > 80%
- **Service Layer**: > 90%
- **Controller Layer**: > 85%
- **Repository Layer**: > 80%

## CI/CD Pipeline

The project uses GitHub Actions for automated:

1. **Testing**: All tests run on every push/PR
2. **Code Coverage**: Coverage reports uploaded to Codecov
3. **Security Scanning**: Trivy vulnerability scanner
4. **Build**: Docker image creation and push
5. **Quality Gates**: Minimum coverage thresholds enforced

### Workflow Triggers
- Push to `main` or `develop` branches
- Pull requests to `main`

## Code Coverage

[![codecov](https://codecov.io/gh/YOUR_USERNAME/smartbet/branch/main/graph/badge.svg)](https://codecov.io/gh/YOUR_USERNAME/smartbet)

Current coverage metrics:
- **Lines**: 85%+
- **Branches**: 80%+
- **Functions**: 90%+

## API Endpoints

### Teams API
```bash
# Get all teams
GET /api/teams/

# Sync teams from external API
POST /api/teams/sync
```

## Project Structure

```
src/
├── main/java/com/outh/backend/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── models/          # JPA entities
│   ├── repository/      # Data access layer
│   ├── services/        # Business logic
│   └── BackendApplication.java
├── main/resources/
│   ├── config/          # Firebase configuration
│   └── application.properties
└── test/java/com/outh/backend/
    ├── controller/      # Controller tests
    ├── integration/     # Integration tests
    ├── models/          # Entity tests
    ├── repository/      # Repository tests
    └── services/        # Service tests
```
