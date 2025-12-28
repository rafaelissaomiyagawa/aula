# Exercise 4: Dockerization & Staging Environment

## Context
A professional workflow often requires two Docker setups:
1.  **Development (`compose.yml`):** Runs only infrastructure (DB, Kafka). The App runs in the IDE (managed by Spring Boot Docker Compose).
2.  **Staging (`compose.staging.yml`):** Runs the **Full Stack** (App + Infra) to simulate production.

## Exercise 1: Containerizing Components

### Task
Create dedicated Dockerfiles to encapsulate your services.

1.  **`app.Dockerfile`**: The application itself.
    -   Use Multi-stage build.
    -   Use Spring Boot `layertools`.
    -   Run as non-root.
2.  **`postgres.Dockerfile`**: Wrapper for the database.
    -   `FROM postgres:17.5`.
3.  **`kafka.Dockerfile`**: Wrapper for the broker.
    -   `FROM confluentinc/cp-kafka:7.6.0`.

---

## Exercise 2: The Staging Environment

### Task
Create `compose.staging.yml`.
1.  Define services: `db`, `zookeeper`, `kafka`, `app`.
2.  Use `build: context: . dockerfile: <file>` for DB, Kafka, and App.
3.  **Important:** Map ports to different values (e.g., 5433, 9093, 8081) to avoid conflicts if you run Dev and Staging simultaneously.
4.  Configure `app` to depend on `db` and `kafka`.

**Verification**:
```bash
docker compose -f compose.staging.yml up --build
```
Access the app at `http://localhost:8081`.

---

## Exercise 3: Development Infrastructure

### Task
Ensure `compose.yml` (the default) ONLY contains infrastructure.
1.  Remove the `app` service.
2.  Keep `db`, `zookeeper`, `kafka`.
3.  This file is used by Spring Boot's `spring-boot-docker-compose` support to automatically start infra when you run `BootRun`.

---

## Production Ready Checklist ✅
- [ ] Dev and Staging environments are separated.
- [ ] Staging environment uses the actual Docker image of the application.
- [ ] Infrastructure is containerized and reproducible.
- [ ] Resource limits are applied in Staging.