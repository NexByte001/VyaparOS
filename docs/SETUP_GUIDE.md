# VyaparOS Setup & Local Development Guide

## Prerequisites

1. **Java Development Kit (JDK 17 or 21 LTS)**
   - Check with `java -version` and `javac -version`
2. **Apache Maven (3.8+)**
   - Check with `mvn -version`
3. **MySQL Server (8.0+)** or In-Memory profile (H2 for fast offline dev/testing)

---

## 1. Environment Configuration

Copy `.env.example` to `.env` or set standard system environment variables:

```bash
# Database Settings
DB_HOST=localhost
DB_PORT=3306
DB_NAME=vyparos_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

# JWT Security
JWT_SECRET=your_super_secret_jwt_key_with_at_least_256_bits_length_here
JWT_EXPIRATION_MS=86400000

# Server & CORS
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://127.0.0.1:5500,http://localhost:8080
```

---

## 2. Running the Backend

```bash
cd backend
mvn clean spring-boot:run
```

The Spring Boot backend will start on `http://localhost:8080`.

---

## 3. Serving the Frontend

You can open `frontend/index.html` directly in your browser or serve it with any lightweight HTTP server (e.g. VS Code Live Server or Python `python -m http.server 3000`).

---

## 4. Default Seed / First Time Setup
1. Register a new user at `/register.html`.
2. Create your first business workspace (e.g. "Bella Salon & Spa").
3. You will immediately be provisioned as `OWNER` with full administrative access.
