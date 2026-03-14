# Auth Service

## 🔐 Overview

The `auth-service` is the identity and access layer for the Cloud Kitchen backend. It handles user registration, login, JWT generation, and role-based protection for different application surfaces like the customer app, kitchen dashboard, and admin panel.

This service is intentionally focused: verify who the user is, issue a token, and make role-aware authorization simple for the rest of the system.

## ✨ What This Service Does

- Registers new users with encrypted passwords
- Authenticates users with username and password
- Issues JWT access tokens
- Supports multiple roles: `USER`, `KITCHEN`, `ADMIN`
- Protects role-specific routes with Spring Security
- Uses stateless authentication with no server session
- Returns clean error responses for common auth failures

## 🧠 Why It Matters

In a multi-service system, auth should be clear, predictable, and reusable. This service provides one place for:

- account creation
- credential verification
- token generation
- role enforcement

That makes it easy for services like `order-engine` to trust the JWT and focus only on business logic.

## 🏗️ Architecture Summary

The service is organized into clear layers:

- `AuthController`: signup and login endpoints
- `UserService`: user creation and authentication logic
- `UserRepository`: user lookup and persistence
- `JwtUtil`: token creation and validation
- `JwtAuthFilter`: reads bearer tokens from requests
- `SecurityConfig`: route protection and filter wiring
- `model`: `User` and `Role`
- `Exceptions`: centralized error handling

## 👥 Supported Roles

The system currently supports three roles:

- `USER`
- `KITCHEN`
- `ADMIN`

These roles are stored with the user and embedded in the JWT as the `role` claim.

## 🔄 Authentication Flow

1. A client signs up using `/auth/signup`.
2. The password is hashed with BCrypt before storage.
3. The client logs in using `/auth/login`.
4. If credentials are valid, the service generates a JWT.
5. The client sends that token in the `Authorization` header.
6. The JWT filter validates the token and loads the user identity into Spring Security.
7. Protected routes allow or reject access based on the user role.

## 🪪 JWT Details

Generated JWTs contain:

- `sub`: username
- `role`: user role
- `iat`: issued-at time
- `exp`: expiration time

Signing algorithm:

- `HS256`

Authorization header format:

```text
Authorization: Bearer <your-jwt-token>
```

## 🌐 Base URL

```text
http://localhost:8081
```

## 📡 API Endpoints

### Public Auth Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/signup` | Register a new user |
| `POST` | `/auth/login` | Authenticate and receive JWT |

### Protected Role Demo Endpoints

| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `GET` | `/user/hello` | `USER` | Test user access |
| `GET` | `/kitchen/hello` | `KITCHEN` | Test kitchen access |
| `GET` | `/admin/hello` | `ADMIN` | Test admin access |

These protected endpoints are simple verification endpoints, but they are useful for proving that JWT-based role authorization is working correctly.

## 🧾 Example Requests

### Signup Request

```json
{
  "username": "parag",
  "password": "password123",
  "role": "USER"
}
```

### Signup Response

```json
{
  "message": "User registered successfully"
}
```

### Login Request

```json
{
  "username": "parag",
  "password": "password123"
}
```

### Login Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## 🛡️ Security Rules

Current route protection in the code:

- `/auth/signup` and `/auth/login` are public
- `/admin/**` requires role `ADMIN`
- `/kitchen/**` requires role `KITCHEN`
- `/user/**` requires role `USER`
- `/h2-console/**` is permitted for development
- everything else requires authentication

The application is stateless, so no HTTP session is used.

## 🔐 Password Handling

User passwords are never stored in plain text.

The service uses:

- `BCryptPasswordEncoder`

That means:

- the raw password is hashed before storing
- login compares the raw password against the stored hash

## 🗃️ Data Model

### User

Each user record contains:

- `id`
- `username`
- `password`
- `role`

The `username` field is unique.

### Role

The role enum currently contains:

- `USER`
- `ADMIN`
- `KITCHEN`

## ⚙️ Configuration

Current runtime properties:

```properties
server.port=8081
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
jwt.secret=${JWT_SECRET:dev-secret-key-change-me-32bytes-min}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### Important Notes

- The service is currently configured with PostgreSQL-style JPA settings.
- The token expiration default is `86400000` milliseconds, which is 24 hours.
- The JWT secret should always be overridden outside local development.

## 🌍 CORS

Allowed origin in current config:

- `http://localhost:5173`

If your frontend runs on another port or domain, update CORS accordingly.

## 🧪 Error Handling

The service returns structured error responses through a global exception handler.

Handled cases include:

- user not found -> `404`
- invalid credentials -> `401`
- forbidden role access -> `403`
- unexpected server error -> `500`

Example error response:

```json
{
  "error": "Invalid credentials"
}
```

## 🚀 Running Locally

From the actual service root:

```bash
cd backend/auth-service/auth-service
mvn spring-boot:run
```

Or build and run:

```bash
mvn clean package
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

## 🧰 Tech Stack

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- JWT with `jjwt`
- Maven
- PostgreSQL driver
- H2 console starter

## 🔗 How It Connects to Other Services

This service is designed to issue JWTs that other backend services can trust.

For example:

- `auth-service` logs in the user and returns a JWT
- `order-engine` accepts the JWT in the `Authorization` header
- `order-engine` reads the username and role from the token
- role-based access is enforced without calling auth on every request

That keeps inter-service communication simpler and makes scaling easier later.

## ✅ What Makes This Auth Service Strong

- clean separation between auth logic and security wiring
- bcrypt password hashing out of the box
- simple and readable JWT flow
- multi-role support
- stateless design
- ready for use by multiple frontend clients
- easy integration point for other microservices

## 🔭 Good Next Steps

If you want to evolve this service further, the most valuable improvements would be:

- add refresh tokens
- add logout or token revocation strategy
- add duplicate username validation with a friendly error message
- add Swagger/OpenAPI docs
- add request validation annotations for signup and login DTOs
- add integration tests for auth flow
- add Docker support
- add user profile endpoints
- add email verification or OTP flow
- add API gateway integration later

## 📁 Project Structure

```text
backend/auth-service/auth-service
|-- src/main/java/com/foodapp/auth_service
|   |-- AuthController
|   |-- Controller
|   |-- DTO
|   |-- Exceptions
|   |-- model
|   |-- security
|   |-- UserRepository
|   `-- UserService
|-- src/main/resources
|   `-- application.properties
|-- pom.xml
`-- README.md
```

## 📝 Summary

The `auth-service` is a focused, role-aware authentication service for the Cloud Kitchen platform. It gives the system a clean identity foundation by combining secure password storage, JWT token generation, and route-level authorization in a simple Spring Boot service.

It is already a solid base for a microservice setup, and it can grow naturally into a more advanced identity service as the platform expands.
