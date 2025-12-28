# Exercise 2: Spring Boot Fundamentals - Advanced Configuration

## Context
Dependency Injection and Inversion of Control are the core of Spring. In production, we often need to:
1.  Configure beans conditionally (e.g., Use real services in Prod, mocks in Dev, or switch providers via config).
2.  Use type-safe configuration properties instead of loose `@Value` annotations.
3.  Structure our configuration classes clearly.

## Objective
Implement a **Notification System** that can switch between SMS and Email providers based on `application.properties`.

---

## Part 1: Type-Safe Configuration

### Task
Create a `NotificationProperties` record to capture:
-   `type`: EMAIL or SMS (Enum)
-   `sender`: String (The "From" address)
-   `enabled`: Boolean

**Requirement**: Use `@ConfigurationProperties` and `@Validated`.

---

## Part 2: Service Interface & Implementations

### Task
1.  Define `NotificationService` interface with `void send(String recipient, String message)`.
2.  Implement `EmailNotificationService`.
3.  Implement `SmsNotificationService`.

---

## Part 3: Conditional Configuration

### Task
Create a `NotificationConfig` class.
-   Use `@Bean` to create the correct `NotificationService` implementation.
-   Use `@ConditionalOnProperty` to check `application.notification.type`.
-   Default to `EMAIL` if the property is missing (`matchIfMissing = true`).

---

## Part 4: Integration

### Task
Inject `NotificationService` into `InventoryService`.
Call `notificationService.send(...)` when an order is successfully placed.

### Verification
1.  Add `application.notification.type=SMS` to `application.properties`.
2.  Run the application.
3.  Place an order (via Swagger or curl).
4.  Check the logs to see if it says `[SMS] ...` or `[EMAIL] ...`.

---

## Production Ready Checklist ✅
- [ ] Used `@ConfigurationProperties` for config.
- [ ] Used Constructor Injection (no `@Autowired` on fields).
- [ ] Logic is decoupled behind an interface (`NotificationService`).
- [ ] Configuration is centralized in a `@Configuration` class.
