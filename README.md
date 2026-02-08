# Multi-Role Auth Service

A Spring Boot–based authentication and authorization service using JWT,
built to support multiple clients such as a Food App, Kitchen Dashboard,
and Admin Panel.

## ✨ Features

- User signup with BCrypt password hashing
- Login with JWT token generation
- Stateless authentication (no sessions)
- Role-based authorization
    - USER
    - KITCHEN
    - ADMIN
- Global exception handling
- H2 database for local development
- Clean, production-style Spring Security configuration

## 🧱 Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (jjwt)
- Spring Data JPA
- H2 Database
- Maven

## 🔐 Authentication Flow

1. User logs in with username & password
2. Server validates credentials
3. JWT is generated containing:
    - username
    - role
    - expiry
4. Client sends JWT in `Authorization` header:
5. JWT filter validates token on every request
6. Access is granted or denied based on role

## 🧪 Sample Endpoints

### Public
- `POST /auth/signup`
- `POST /auth/login`

### Protected
- `GET /user/hello` (USER)
- `GET /kitchen/hello` (KITCHEN)
- `GET /admin/hello` (ADMIN)

## 🚀 Future Enhancements

- Refresh tokens
- Token revocation / logout
- MySQL / PostgreSQL support
- Dockerization
- Integration with Food App microservices
