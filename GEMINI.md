# Gemini Agent Instructions

## Core Mandates

- **Review First:** Always propose a plan and wait for user approval before implementing changes.
- **Production Ready:** When working on topics, always check for and suggest missing production-ready configurations, best practices, and patterns.

## Memory
- **Topic 1: Gradle (Automation, Dependencies, and Plugins)**
    - Migrated to Version Catalogs (`libs.versions.toml`).
    - Added Spotless (Google Java Format) and Versions plugins.
    - Created Exercise 01 in `docs/exercises/01-gradle-automation.md`.
    - Consolidated `kafka.yml` into `compose.yml` for unified development environment via Spring Boot Docker Compose.
- **Topic 2: Spring Boot Fundamentals (DI, IoC, Configuration)**
    - Implemented `NotificationService` (Email/SMS) using `@ConditionalOnProperty`.
    - Created `NotificationProperties` for type-safe config.
    - Created Exercise 02 in `docs/exercises/02-spring-fundamentals.md`.
    - Implemented `OrderPlacedEvent` and `OrderNotificationListener` to decouple notification logic.
    - Created Exercise 03 in `docs/exercises/03-spring-events.md` (Replaces Price Calculator exercise).
- **Topic 3: Docker Integration**
    - Created optimized Multi-stage `Dockerfile` with layered jar support.
    - Implemented `.dockerignore` and resource limits in `compose.yml`.
    - Configured `depends_on` with healthcheck conditions in Docker Compose.
    - **Refactoring:** Moved all Docker files to `docker/` directory.
    - **Refactoring:** Configured `spring.docker.compose.file` to point to `./docker/compose.yml`.
    - Created Exercise 04 in `docs/exercises/04-docker-integration.md`.






