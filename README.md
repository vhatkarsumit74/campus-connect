# ConnectCampus — College Orchestration Platform

A full‑stack project (Mumbai University) to help **students** discover eligible colleges, manage bookmarks, read blogs, and raise tickets — while **admins** manage colleges, programs, cutoffs, blogs, and tickets.

---

## ✨ Features

### Student

* Register / Login / **Forgot & Reset Password** (JWT auth)
* Profile dashboard (CET/JEE percentiles, 12th marks)
* **Eligibility search** by provided percentile or using saved profile
* Browse colleges, view details (programs & year‑wise CET/JEE cutoffs)
* **Contact college** directly from the college page (creates a ticket, optional email to college)
* General **support tickets** with threaded messages
* **Bookmarks** for colleges
* Read blogs + add comments

### Admin

* Manage **users** (list, details, delete, set role, verify)
* Manage **colleges** (CRUD) with programs & cutoffs
* Manage **blogs** (CRUD) and moderate comments
* Manage **tickets** (triage by status, reply, close)

---

## 🧱 Tech Stack

* **Backend:** Spring Boot 3.5.x (Java 17), Spring Web, JPA/Hibernate, Validation, Security (JWT), Mail, Cache
* **Database:** PostgreSQL
* **Docs:** springdoc‑openapi ➜ Swagger UI
* **Build:** Maven
* **Frontend:** React (separate app; calls this API)

---

## 📦 Project Structure (backend)

```
com.aura.connectcampus
 ├─ ConnectCampusApplication.java
 ├─ auth/              # JWT, login/register, password reset
 ├─ blogs/             # blog posts, comments, admin/public controllers
 ├─ bookmarks/         # student bookmarks
 ├─ colleges/          # College, Program, Cutoff + eligibility controllers
 ├─ common/            # MailService, exceptions, handlers
 ├─ config/            # Security config, CORS
 ├─ security/          # CurrentUser helper
 └─ users/             # user entity, profile & admin controllers
```

---

## 🚀 Getting Started

### Prerequisites

* **Java 17** (SDK)
* **Maven** 3.9+
* **PostgreSQL** 13+
* (Optional) **Node.js** 18+ for the React frontend

### 1) Clone & open

```bash
git clone <your-repo-url>
cd connectcampus
```

### 2) Create PostgreSQL DB & user

```sql
-- psql
CREATE DATABASE connectcampus;
CREATE USER connectcampus WITH ENCRYPTED PASSWORD 'connectcampus';
GRANT ALL PRIVILEGES ON DATABASE connectcampus TO connectcampus;
```

### 3) Configure application properties

Create `src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/connectcampus
spring.datasource.username=connectcampus
spring.datasource.password=connectcampus
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2

spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# JWT
jwt.issuer=connectcampus
# Use a 32+ byte secret in prod
jwt.secret=please-change-me-32-bytes-minimum-abc12345
jwt.expiryMinutes=120

# OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui

# Mail (dev: logs to console if JavaMailSender not configured)
app.mail.from=noreply@connectcampus.local
# Password reset link target (your React route)
app.reset.base-url=http://localhost:5173/reset-password
app.reset.expiry-minutes=30

# Optional fallback: receive college-contact emails when college has no email
app.admin.email=admin@connectcampus.local

# Uploads (optional)
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging (dev)
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.orm.jdbc.bind=TRACE
```

> **Note:** This project currently uses `spring.jpa.hibernate.ddl-auto=update` so tables are created from entities automatically. For production, switch to Flyway + `validate`.

### 4) Build & run

```bash
# From project root
mvn -U clean spring-boot:run
```

Now open **Swagger UI**: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

---

## 🔐 Security Model (JWT)

* Public endpoints: `GET /api/colleges/**`, `GET /api/blogs/**`, `GET /v3/api-docs/**`, `GET /swagger-ui/**`
* Student‑authenticated: profile, bookmarks, tickets, blog comments, contact‑college
* Admin‑only: all `/api/admin/**` endpoints

### First admin user

1. **Register** a normal account via `POST /api/auth/register`
2. Promote it to ADMIN with a quick SQL update:

```sql
UPDATE users SET role='ADMIN' WHERE email='your@email.com';
```

---

## 🧭 API Overview

Use Swagger UI for full, live docs.

### Key Endpoints (sample)

| Method | Path                                               | Purpose                                      |
| ------ | -------------------------------------------------- | -------------------------------------------- |
| POST   | `/api/auth/register`                               | Register & return JWT                        |
| POST   | `/api/auth/login`                                  | Login & return JWT                           |
| POST   | `/api/auth/forgot`                                 | Send reset link (email/log)                  |
| POST   | `/api/auth/reset`                                  | Reset password with token                    |
| GET    | `/api/me`                                          | Get my profile                               |
| PUT    | `/api/me`                                          | Update profile (marks/percentiles)           |
| GET    | `/api/eligibility`                                 | Eligibility by provided percentile           |
| GET    | `/api/eligibility/mine`                            | Eligibility using saved profile              |
| GET    | `/api/colleges`                                    | Public list (filters + pageable)             |
| GET    | `/api/colleges/{id}`                               | College details (programs, cutoffs)          |
| POST   | `/api/colleges/{id}/contact`                       | Contact a college (creates ticket; emails)   |
| GET    | `/api/blogs`                                       | List blog posts                              |
| GET    | `/api/blogs/{slug}`                                | Blog details + comments                      |
| POST   | `/api/blogs/{slug}/comments`                       | Add a comment (student)                      |
| GET    | `/api/bookmarks`                                   | My bookmarks                                 |
| POST   | `/api/bookmarks/{collegeId}`                       | Bookmark                                     |
| DELETE | `/api/bookmarks/{collegeId}`                       | Unbookmark                                   |
| POST   | `/api/tickets`                                     | Create ticket (general/college)              |
| GET    | `/api/tickets`                                     | My tickets (pageable)                        |
| POST   | `/api/tickets/{id}/messages`                       | Add message to my ticket                     |
| GET    | `/api/admin/colleges`                              | (via Swagger try‑it) list after you add some |
| POST   | `/api/admin/colleges`                              | Create college (ADMIN)                       |
| POST   | `/api/admin/colleges/{collegeId}/programs`         | Add program (ADMIN)                          |
| POST   | `/api/admin/colleges/programs/{programId}/cutoffs` | Add cutoff (ADMIN)                           |
| GET    | `/api/admin/users`                                 | List users (ADMIN)                           |
| PATCH  | `/api/admin/users/{id}/role`                       | Set user role (ADMIN)                        |
| GET    | `/api/admin/tickets`                               | List tickets (filter by status)              |

> A full API catalog CSV is available. Place it under `docs/api-catalog.csv` in your repo (you can import the one generated during development).

---

## 📧 Emails & Password Reset

* In development, if no SMTP is configured, all emails are **logged to console** by `MailService` (see logs for your reset link).
* Configure `spring.mail.*` if you want real email sending.

**Password reset flow:**

1. `POST /api/auth/forgot` with your email
2. Click the link (token) sent to your email/log
3. `POST /api/auth/reset` with `{ token, newPassword }`

---

## 🌐 CORS (for React)

CORS is enabled for `http://localhost:5173` and `http://localhost:3000` via `CorsConfig`. Adjust those origins as needed.

---

## 🗃️ Data Model (high‑level)

* **users** `(role, verified, percentiles, marks)`
* **colleges** `(city, state, fees, email, website, phone)`
* **programs** `→ colleges`
* **cutoffs** `→ programs` `(exam=CET/JEE, year, percentileCutoff)`
* **blog\_posts** & **blog\_comments**
* **bookmarks** `(user_id + college_id)`
* **contact\_tickets** & **contact\_messages** `(optional college_id)`
* **password\_reset\_tokens** `(UUID id + secret hash)`

> Tables are created automatically on first run (Hibernate update). For production, switch to Flyway.

---

## 🧪 Quick Smoke Tests (curl)

```bash
# Register
curl -s -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"alice@example.com","password":"Secret123","firstName":"Alice"}'

# Login (capture token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"alice@example.com","password":"Secret123"}' | jq -r .token)

# Update profile
curl -s -X PUT http://localhost:8080/api/me \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"cetPercentile":95.2,"jeePercentile":88.4}'

# Eligibility using saved profile
curl -s 'http://localhost:8080/api/eligibility/mine?exam=CET&year=2025' -H "Authorization: Bearer $TOKEN"
```

---

## 🐳 Docker (Postgres dev only)

`docker-compose.yml` snippet to start Postgres locally:

```yaml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: connectcampus
      POSTGRES_USER: connectcampus
      POSTGRES_PASSWORD: connectcampus
    ports:
      - "5432:5432"
    volumes:
      - pg_data:/var/lib/postgresql/data
volumes:
  pg_data:
```

Run the app separately with Maven (`spring-boot:run`).

---

## 🧰 Troubleshooting

* **Port 8080 already in use:**

  ```bash
  lsof -i :8080
  kill -9 <PID>
  ```
* **DB connection failures:** Verify `spring.datasource.*`, Postgres is running, and credentials are correct.
* **JWT 401s:** Make sure you’re sending `Authorization: Bearer <token>`.
* **Admin calls fail:** Promote your user to ADMIN in the DB.

---

## 🗺️ Roadmap (nice‑to‑have)

* CSV **bulk import** for colleges/programs/cutoffs
* Rate limiting for ticket/comment spam
* Soft‑delete & audit columns
* OpenAPI examples & response schemas

---

## 📄 License

MIT (add a `LICENSE` file if you want to publish as open source).

---

**Happy building!** If you want this README turned into a GitHub‑friendly wiki or split into `/docs` pages, say the word and we’ll scaffold it.
