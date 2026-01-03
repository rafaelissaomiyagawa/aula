# Exercise 14: Integration Testing "The Order Tracking API"

## Context
The "Order Status Checker" API (`GET /api/orders/{orderNumber}`) is a critical customer-facing feature. We need integration tests to ensure it is robust, correctly handling both valid and invalid inputs. This includes verifying successful data retrieval and proper 404 Not Found errors, ensuring our `GlobalExceptionHandler` is working as expected in a real HTTP environment.

## Objective
Test a REST endpoint that uses a path variable and properly handles "Not Found" scenarios, validating the integration between the controller, service, database, and exception handler.

---

### Part 1: Test Valid Order Retrieval

**Feature Request:** "As a customer, I want to use my order number to successfully retrieve my order details."

**Task:**
1.  Create a new test class `OrderControllerIntegrationTest` extending `BaseIntegrationTest`.
2.  Write a test method: `shouldReturnOrderDetailsWhenGivenValidOrderNumber()`.
3.  **Arrange:**
    *   In your `@BeforeEach` or test method, use `testDataInitializer.insertOrder(...)` or an SQL script to create a known `Order` with a specific `orderNumber` (e.g., "TEST-12345").
4.  **Act:**
    *   Perform a `GET` request to `/api/orders/TEST-12345` using `MockMvc`.
5.  **Assert:**
    *   Verify the HTTP status is `200 OK`.
    *   Use `jsonPath` to assert that `$.orderNumber` in the response matches "TEST-12345".
    *   Assert that other key fields, like `customerEmail`, are present and correct.

---

### Part 2: Test "Not Found" Scenario

**Feature Request:** "As a system administrator, I want to ensure that looking up an invalid order number returns a clean 404 error, not a system crash."

**Task:**
1.  In `OrderControllerIntegrationTest`, write a new test: `shouldReturn404NotFoundForInvalidOrderNumber()`.
2.  **Act:**
    *   Perform a `GET` request to `/api/orders/INVALID-ORDER-NUM`.
3.  **Assert:**
    *   Verify the HTTP status is `404 NOT_FOUND`.
    *   Use `jsonPath` to assert that the error response body contains the message from your `GlobalExceptionHandler`, e.g., `$.error` equals `"Order not found with number: INVALID-ORDER-NUM"`.

---

### Part 3: Test Input Validation (Bonus)

**Feature Request:** "As a developer, I want to ensure that attempts to update stock with invalid data are rejected with a helpful error message."

**Task:**
1.  You will need an endpoint for updating stock, for example, `PATCH /api/products/{id}/stock` with a body like `{"newQuantity": -5}`.
2.  Write a test method: `shouldReturn400BadRequestForNegativeStockUpdate()`.
3.  **Act:**
    *   Perform the `PATCH` request with an invalid negative quantity.
4.  **Assert:**
    *   Verify the HTTP status is `400 BAD_REQUEST`.
    *   Use `jsonPath` to check that the response body contains the validation error message from `GlobalExceptionHandler`, e.g., `$.newQuantity` equals `"must be greater than or equal to 0"`.

---

## Production Ready Checklist ✅
- [ ] Happy path (200 OK) for the endpoint is tested.
- [ ] Sad path (404 Not Found) for the endpoint is tested.
- [ ] Error response body is verified to ensure the `RestControllerAdvice` is working.
- [ ] Input validation errors (400 Bad Request) are tested at the integration level.
