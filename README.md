# 🎓 Academic Excellence Platform

A cutting-edge Score Management System built on Spring Boot, designed to revolutionize academic performance tracking and analysis. This enterprise-grade application leverages Domain-Driven Design (DDD) principles to deliver a comprehensive solution for educational institutions, enabling real-time score management, in-depth analytics, and data-driven decision making.

## 🏆 Key Features

### 📊 Advanced Score Management
- **Comprehensive Gradebook**: Intuitive interface for managing student scores across multiple subjects and semesters
- **Automated Calculations**: Real-time GPA/CGPA calculation with support for various grading scales
- **Bulk Operations**: Import/export scores in multiple formats (Excel, CSV) for efficient data management
- **Custom Grading Systems**: Flexible configuration for different assessment types and weightings
- **Performance Analytics**: Interactive dashboards with visual insights into student and class performance

### 🎯 Core Capabilities
- **Role-Based Access Control**: Granular permissions for administrators, teachers, and students
- **Real-time Updates**: WebSocket-powered live score updates and notifications
- **Document Management**: Secure storage and retrieval of score-related documents and reports
- **Audit Trail**: Complete history of all score modifications with user attribution
- **Data Visualization**: Interactive charts and graphs for performance trend analysis

### 🚀 Advanced Technical Features

#### 🛠 Auto-Generated REST APIs
- **Zero-Config CRUD**: Automatically generates REST endpoints for all entities
- **Dynamic Filtering**: Powerful query capabilities with automatic filter generation
  - Support for complex queries: `?filter=score>80&sort=studentName,desc`
  - Field selection: `?fields=id,name,score&sort=createdAt,desc`
  - Pagination and sorting built-in

#### ⚡ Intelligent Caching
- **Auto-Caching Layer**: Smart caching strategy with Redis
  - Automatic cache invalidation on data changes
  - Time-to-live (TTL) and size-based eviction policies
  - Cache statistics and monitoring

#### 🔍 Smart Data Processing
- **Dynamic Filtering**: Advanced query capabilities
  - JPA Specification-based filtering
  - Nested property filtering
  - Custom filter expressions
- **Projection Support**: Request only needed fields
  - Reduces network payload
  - Improves query performance

#### 🔐 Robust Security
- **Keycloak Integration**: Enterprise-grade Identity and Access Management
  - Centralized authentication and authorization
  - Single Sign-On (SSO) across all services
  - User Federation with LDAP/Active Directory
  - Fine-grained authorization with custom policies
  - User self-service account management

- **OAuth2 & JWT Integration**: Secure API Access
  - Social login support (Google, GitHub, etc.)
  - Role-based (RBAC) and attribute-based (ABAC) access control
  - Token-based authentication with refresh tokens
  - Secure password policies and encryption
  - Multi-factor authentication (MFA) support

#### 🏗 Modern Architecture
- **Spring Boot 3.1+**: Latest framework features
- **Reactive Programming**: Non-blocking I/O for better scalability
- **Container Ready**: Docker and Kubernetes support
- **Cloud-Native**: Easy deployment on any cloud platform

#### 📊 Monitoring & Observability
- **Actuator Endpoints**: Health checks, metrics, and more
- **Distributed Tracing**: Track requests across services
- **Log Aggregation**: Centralized logging with ELK stack
- **Performance Metrics**: Prometheus and Grafana integration

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7.6+ or Maven 3.6+
- PostgreSQL 13+
- Kafka (for event streaming)
- Redis (for caching)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd core
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```
   or with Maven:
   ```bash
   mvn clean install
   ```

3. **Configure the application**
   Copy the example configuration and update with your settings:
   ```bash
   cp src/main/resources/application-example.yml src/main/resources/application.yml
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```
   or with Maven:
   ```bash
   mvn spring-boot:run
   ```

## ⚙️ Configuration

### Application Properties

Key configurations in `application.yml`:

```yaml
# Server Configuration
server:
  port: 8080
  servlet:
    context-path: /api

# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/core
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

# JWT Configuration
app:
  jwt:
    secret: your-secret-key
    expiration-ms: 3600000
    refresh-expiration-ms: 86400000

# Kafka Configuration
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: core-group

# Redis Configuration
spring:
  redis:
    host: localhost
    port: 6379
```

### Environment Variables

You can override any configuration using environment variables. For example:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/core
export SPRING_DATASOURCE_USERNAME=db_user
export SPRING_DATASOURCE_PASSWORD=db_password
```

## 📚 Modules

### Core Module
Contains the main application logic, entities, and services.

### Stream Module
Handles event streaming and logging using Kafka. See [STREAM-MODULE.md](README-STREAM-MODULE.md) for detailed documentation.

## 🔧 API Documentation

- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v3/api-docs`

## 🛠 Development

### Code Style

This project uses Google Java Style Guide. Before committing, run:

```bash
./gradlew spotlessApply
```

### Testing

Run all tests:

```bash
./gradlew test
```

### Database Migrations

This project uses Flyway for database migrations. Place your SQL scripts in `src/main/resources/db/migration/`.

## 📦 Dependencies

- **Spring Boot**: Core application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database access
- **Spring Kafka**: Event streaming
- **Redis**: Caching
- **ModelMapper**: Object mapping
- **Lombok**: Boilerplate reduction
- **PostgreSQL**: Database

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

- `src/main/java/com/vuog/core`: Main application code.
- `src/test/java/com/vuog/core`: Test cases.
- `src/main/resources`: Configuration files and static resources.

### Adding a New Module

1. Create a new package under `com.vuog.core.module`.
2. Define your domain, repository, service, and controller layers.
3. Register your module in the application context if necessary.

### Running Tests

```bash
mvn test
```

## Contributing

Contributions are welcome! Please follow the standard GitHub workflow:

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For questions or support, please contact [your-email@example.com].
