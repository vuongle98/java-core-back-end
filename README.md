# Core Application

The **Core Application** is a modular Spring Boot application designed to provide a foundation for building scalable and maintainable systems. It includes features such as user management, file storage, RESTful APIs, and dynamic projections.

## Features

- **User Management**: Manage users with role-based access control.
- **File Storage**: Upload, update, download, and delete files with metadata support.
- **Dynamic REST API**: Generic REST services with filtering and projection capabilities.
- **Report Generation**: Export reports in multiple formats (PDF, CSV, XLSX).
- **Authentication & Authorization**: Secure endpoints with JWT-based authentication.
- **Modular Design**: Easily extendable architecture.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (or any supported database)

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd core
```

### Build the Project

```bash
mvn clean install
```

### Run the Application

```bash
mvn spring-boot:run
```

### Access the Application

- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (if enabled)

## Configuration

### Application Properties

The application can be configured using the `application.properties` or `application.yml` file located in the `src/main/resources` directory.

Example:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/core
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt.secret=your_jwt_secret
app.jwt.expiration.ms=3600000
app.jwt.refersh.expiration.ms=86400000

# File Storage
app.storage-location=uploads
```

### Environment Variables

You can override configurations using environment variables.

## Usage

### User Management

- **Create User**: `POST /api/user` with a JSON payload.
- **Get Users**: `GET /api/user` with optional filters.
- **Update User**: `PUT /api/user/{id}`.
- **Delete User**: `DELETE /api/user/{id}`.

### File Storage

- **Upload File**: `POST /api/files` with a multipart file.
- **Download File**: `GET /api/files/{id}/download`.

### Report Generation

- **Generate Report**: `POST /api/report/generate` with a `ReportRequest` payload.
- **Supported Formats**: PDF, CSV, XLSX.

### Authentication

- **Login**: `POST /api/auth/token` with username and password.
- **Refresh Token**: `POST /api/auth/refresh`.
- **Logout**: `POST /api/auth/logout`.

## Development

### Code Structure

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
