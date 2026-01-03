# Exercise 12: TDD - Refund Policy Enforcement

## Context
The finance department needs a strict test suite around the `processRefund` method. The two most critical rules are the 20% restocking fee for `ELECTRONICS` and the 2-day return window for `GROCERY` items. We will use TDD to build tests that enforce these policies.

## Objective
Use TDD and advanced mocking techniques (including time-based mocking) to test complex business logic involving state, dates, and side effects (like stock replenishment).

---

### Part 1: The "Red" Phase - Test the Electronics Restocking Fee

**Task:**
1.  Create a new unit test class: `RefundPolicyTddTest`. Annotate it for Mockito.
2.  **Write a failing test method:** `shouldApplyRestockingFeeForElectronicsRefund()`.
3.  **Arrange:**
    *   Mock `OrderRepository` to return a sample `Order` containing an `OrderItem` for an `ELECTRONICS` product. The item's price should be $100 for easy calculation.
4.  **Act:**
    *   Call `orderService.processRefund()`.
5.  **Assert:**
    *   Assert that the returned `BigDecimal` refund amount is exactly `$80.00`.
6.  **Run the test.** Assuming the logic is correct, this test should pass. To practice TDD, **comment out the restocking fee logic** in `OrderService.java`. Run the test again. It **MUST FAIL**. This is our **RED** light.

---

### Part 2: The "Green" Phase - Re-implement the Logic

**Task:**
1.  Un-comment the restocking fee logic in `OrderService.java`.
2.  **Run the test again.** It **MUST PASS**. This is our **GREEN** light.

---

### Part 3: "Red-Green-Refactor" for the Grocery Rule

This rule depends on the current date, which makes it hard to test reliably. We must control time itself!

**Task:**
1.  **Write a failing test:** `shouldThrowExceptionWhenGroceryRefundIsOutside2DayWindow()`.
2.  **Arrange:**
    *   Create an `Order` with a `GROCERY` item.
    *   Set the `orderDate` to be `LocalDateTime.now().minusDays(3)`.
    *   Mock the `orderRepository` to return this "old" order.
3.  **Act & Assert:**
    *   Use `assertThrows(BusinessRuleException.class, ...)` when calling `orderService.processRefund()`.
4.  **Run the test.** It should fail if the logic is missing. Write the logic in `OrderService` to make it pass.
5.  **Refactor:** Is there a way to make time-based testing cleaner? (This is an advanced topic, but worth thinking about. You could inject a `Clock` object instead of using `LocalDateTime.now()` directly).

---

### Part 4: Verifying Side Effects

A good unit test also verifies side effects. The `processRefund` method should call `productRepository.save()` to replenish stock.

**Task:**
1.  In your successful refund tests (like the electronics test), add a verification step at the end.
2.  Use `Mockito.verify()` to check that `productRepository.save()` was called **exactly one time**.
3.  Use an `ArgumentCaptor<Product>` to capture the `Product` object that was saved. Assert that its `stockQuantity` has been correctly increased.

---

## Production Ready Checklist ✅
- [ ] Tests for financial calculations are precise.
- [ ] Time-sensitive logic is tested reliably.
- [ ] Side effects (like saving to a repository or publishing an event) are verified.
- [ ] `Mockito.verify()` and `ArgumentCaptor` are used to check behavior, not just state.
