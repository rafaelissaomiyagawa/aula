# Exercise 3: Evolutionary Architecture & Strategy Pattern

## Context
Applications evolve. You might start with a Monolith (internal Spring Events) and move to Microservices (Kafka).
Hardcoding implementation details (like `ApplicationEventPublisher` or `KafkaTemplate`) inside your Domain Service (`InventoryService`) makes this evolution painful and risky.

## Objective
Implement a **Publisher Strategy** that allows switching between "Local Events" and "Kafka Events" without changing the core `InventoryService`.

---

## Part 1: Define the Interface (The Contract)

### Task
Create an `InventoryEventPublisher` interface.
-   Method: `void publishOrderPlaced(Order order)`
-   Method: `void publishStockUpdated(StockUpdate update)`

**Why:** `InventoryService` only cares *that* an event is published, not *how*.

---

## Part 2: Implement Strategies

### Task 1: Internal Strategy (Default)
Create `SpringInventoryEventPublisher`.
-   Implement the interface.
-   Inject `ApplicationEventPublisher`.
-   Publish `OrderPlacedEvent` and `StockUpdatedEvent`.
-   Annotate with `@Primary` to make it the default injection choice.

### Task 2: External Strategy
Create `KafkaInventoryEventPublisher`.
-   Implement the interface.
-   Inject `KafkaEventProducer`.
-   Send the order and stock update to Kafka topics.
-   Annotate with `@ConditionalOnProperty(name="app.events.strategy", havingValue="kafka")`.

---

## Part 3: Refactor the Service

### Task
Update `InventoryService`.
-   Remove `KafkaProducerService` (or `KafkaEventProducer`) dependency entirely.
-   Inject `InventoryEventPublisher`.
-   Call `eventPublisher.publishOrderPlaced(...)` and `eventPublisher.publishStockUpdated(...)`.

---

## Part 4: Verification

### Task
1.  **Default Mode:** Run the app. Place an order. Check that `OrderNotificationListener` receives the event.
2.  **Kafka Mode:** Note that since we used `@Primary` for the Spring implementation, simply adding the property might not be enough if both beans are active.
    *   *Challenge:* How would you force the Kafka implementation to take precedence? (Hint: `@Profile`, or using `@ConditionalOnProperty` on the Spring bean too instead of `@Primary`).
    *   *Observation:* For this exercise, we prioritize the "Spring Way" (`@Primary`), so the local event always fires unless we explicitly change the code or use Profiles to exclude it.

---

## Production Ready Checklist ✅
- [ ] **Dependency Inversion:** High-level modules (`InventoryService`) depend on abstractions (`InventoryEventPublisher`), not low-level details (`Kafka`).
- [ ] **Clean Code:** `InventoryService` is focused on business rules, not infrastructure (Kafka/Spring Context).
- [ ] **Evolutionary Design:** We migrated from direct Kafka usage to an abstraction layer without breaking the domain logic.
