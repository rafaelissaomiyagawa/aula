# Exercise 11: TDD - Order Discount & Shipping Rules

## Context
Our sales department wants to introduce a new business rule: "Orders for `BOOKS` over $50 get a 5% discount on the total amount." We need to implement this rule using TDD to ensure it works correctly and doesn't break existing logic. We will also add a test for the existing free shipping rule.

## Objective
Use the TDD workflow to add a new discount rule and create a regression test for an existing shipping rule, focusing on mocking and asserting calculated values.

---

### Part 1: The "Red" Phase - Test the New Discount

**Task:**
1.  Create a new unit test class: `OrderPlacementTddTest`. Annotate it for Mockito.
2.  **Write a failing test method:** `shouldApplyBookDiscountForOrdersOver50()`.
3.  **Arrange:**
    *   Mock `ProductRepository` to return a `Product` of category `BOOKS` with a price of $60.
    *   Create an `OrderRequest` for this book.
4.  **Act:**
    *   Call `orderService.placeOrder()`.
5.  **Assert:**
    *   Assert that the `totalAmount` on the returned `OrderResponse` is `$57.00` (i.e., $60 * 0.95). Use `assertEquals` with `BigDecimal`.
6.  **Run the test.** It **MUST FAIL**. The assertion for the total amount will be incorrect because the discount logic doesn't exist yet. This is our **RED** light.

---

### Part 2: The "Green" Phase - Implement the Discount

**Task:**
1.  Go to `OrderService.java`.
2.  Modify the `placeOrder` method.
3.  After calculating the `totalAmount` but before saving the order, add an `if` block to check if the order contains only books and if the total exceeds $50. If so, apply the 5% discount.
4.  **Run the test again.** It **MUST PASS**.

---

### Part 3: The "Refactor" Phase - Regression Test for Existing Rules

Now that you've added a new feature, let's ensure we didn't break anything. We'll add a TDD-style test for the existing "Free Shipping" rule.

**Task:**
1.  **Write a failing test first (conceptually):** In the same test class, write a test `shouldEnableFreeShippingForOrdersOver100()`.
2.  Mock products to create an order with a total of $101.
3.  Assert that `order.isFreeShipping()` is true.
4.  **Run the test.** This test *should* pass immediately because the production code already exists. This demonstrates how TDD builds a **regression safety net**.
5.  **Refactor:** Look at your `placeOrder` logic. Can you extract the discount calculation or shipping logic into private helper methods to make the main method cleaner? Run all tests to ensure your refactoring is safe.

---

## Production Ready Checklist ✅
- [ ] New features are driven by failing tests.
- [ ] Existing features are protected by regression tests.
- [ ] Tests use mocks to isolate the service layer from the database.
- [ ] `BigDecimal` values are compared correctly in tests.
