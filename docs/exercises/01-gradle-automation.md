# Exercise 1: Gradle Modernization & Automation

## Context
A production-ready project should have centralized dependency management and automated code quality checks. Hardcoding versions in `build.gradle` makes updates difficult and prone to errors.

## Objective
Refactor the build system to use **Gradle Version Catalogs** and add automation plugins for dependency management and code formatting.

---

## Part 1: Centralizing Dependencies (Version Catalog)

### Task
1. Create a file at `gradle/libs.versions.toml`.
2. Move all hardcoded versions from `build.gradle` to this file.
3. Update `build.gradle` to use the `libs` alias.

### Expected Catalog Structure
```toml
[versions]
springBoot = "3.5.8"
# ... other versions

[libraries]
flyway-core = { group = "org.flywaydb", name = "flyway-core", version.ref = "flyway" }
# ... other libraries

[plugins]
spring-boot = { id = "org.springframework.boot", version.ref = "springBoot" }
# ... other plugins
```

---

## Part 2: Dependency Hygiene

### Task
Add the `com.github.ben-manes.versions` plugin to the catalog and `build.gradle`.

### Verification
Run the following command to see which dependencies are outdated:
```bash
./gradlew dependencyUpdates
```

---

## Part 3: Automated Formatting (Spotless)

### Task
1. Add the `com.diffplug.spotless` plugin.
2. Configure it in `build.gradle` to use `googleJavaFormat()`.

### Verification
Run:
```bash
./gradlew spotlessCheck # Should fail if code is messy
./gradlew spotlessApply # Automatically fixes formatting
```

---

## Production Ready Checklist ✅
- [ ] Dependencies are NOT hardcoded in `build.gradle`.
- [ ] Versions are managed in a single `.toml` file.
- [ ] Code style is enforced via build task.
- [ ] There is a standard way to check for library updates.
