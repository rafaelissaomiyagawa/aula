# Exercise 10: TDD - Product Stock Validation

## Context
The `Product` entity has a `stockQuantity`. Our `ProductService` has an `updateStock` method, but it currently lacks a critical safety check: the final stock must never be negative. We will use the **Test-Driven Development (TDD)** workflow to add this feature safely.

## Objective
Use the **Red-Green-Refactor** cycle to implement a business rule that prevents stock from becoming negative.

---

### Part 1: The "Red" Phase - Write a Failing Test

**Task:**
1.  Navigate to the `test/` source set and create a new unit test class: `ProductServiceTest`.
2.  **Write a failing test method:** `shouldThrowExceptionWhenStockUpdateResultsInNegative()`.
3.  **Arrange:**
    *   Use `@ExtendWith(MockitoExtension.class)` on your test class.
    *   Use `@Mock` to create mock instances of `ProductRepository` and `InventoryEventPublisher`.
    *   Use `@InjectMocks` to create an instance of `ProductService` with the mocks injected.
    *   In your test method, mock the `productRepository` to return a `Product` with a `stockQuantity` of `10`.
4.  **Act & Assert:**
    *   Call `productService.updateStock()` with a `quantityChange` of `-20`.
    *   Use `Assertions.assertThrows()` to verify that a `BusinessRuleException` is thrown.
5.  **Run the test.** It **MUST FAIL**. It will likely not throw any exception at all. This is our **RED** light.

---

### Part 2: The "Green" Phase - Make it Pass

**Task:**
1.  Go to the production code: `ProductService.java`.
2.  Modify the `updateStock` method.
3.  Add the `if (newStock < 0)` check and throw the `BusinessRuleException`.
4.  **Run the test again.** It **MUST PASS**. The feature is now correctly implemented according to the test. This is our **GREEN** light.

---

### Part 3: The "Refactor" Phase - Clean Up

**Task:**
1.  Look at your `updateStock` method. Is the code clean and easy to understand?
2.  Look at your test. Is it readable? Can any part be simplified?
3.  Run the tests one last time to ensure your refactoring didn't break anything.

---

## Production Ready Checklist ✅
- [ ] A failing test was written first.
- [ ] The test now passes without changing the test itself.
- [ ] Production code is clean and readable.
- [ ] The business rule is protected by a regression test.
