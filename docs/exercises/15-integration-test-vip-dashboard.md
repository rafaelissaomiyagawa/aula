# Exercise 15: Integration Testing "The VIP Customer Dashboard"

## Context
The Marketing team's new "VIP Customer Dashboard" uses a complex JPQL query with aggregations (`SUM`, `COUNT`, `GROUP BY`, `HAVING`). Unit tests can mock the repository, but only an integration test can prove that the query runs correctly against a real PostgreSQL database and that the full API works end-to-end.

## Objective
Write an integration test that verifies a complex, read-only "reporting" API. This involves preparing a specific and varied dataset and asserting that the API response correctly reflects the aggregated data from the database.

---

### Part 1: Expose the VIP Customer API

**Task:**
1.  In `OrderService`, create a method `getVipCustomers(Long minOrders, BigDecimal minSpent)` that calls `orderRepository.findVipCustomers()`.
2.  In `OrderController`, add a `GET /api/orders/vip-customers` endpoint.
3.  This endpoint should accept two `@RequestParam` values: `minOrders` and `minSpent`.
4.  The endpoint should call the service method and return a `List<VipCustomerDTO>`.

---

### Part 2: Prepare a Rich Test Dataset

This is the most critical part of testing a reporting feature. The data must cover all edge cases.

**Task:**
1.  In your `TestDataInitializer` or a dedicated SQL script for this test, insert data for at least three customers:
    *   **Customer A (VIP):** Create a customer with **5 orders** and a **total spend of $1200**. This customer should appear in the results.
    *   **Customer B (Not VIP - Not enough orders):** Create a customer with **2 orders** and a **total spend of $1500**. This customer should NOT appear.
    *   **Customer C (Not VIP - Not enough spend):** Create a customer with **5 orders** and a **total spend of $500**. This customer should NOT appear.
2.  Ensure your `@BeforeEach` method calls the data initialization logic.

---

### Part 3: Test the VIP API

**Task:**
1.  In `OrderControllerIntegrationTest`, write a test method: `shouldReturnOnlyVipCustomersMatchingCriteria()`.
2.  **Act:**
    *   Perform a `GET` request to `/api/orders/vip-customers?minOrders=4&minSpent=1000` using `MockMvc`.
3.  **Assert:**
    *   Verify the HTTP status is `200 OK`.
    *   Use `jsonPath` to assert that the response is an array of size `1` (`$.length()`).
    *   Assert that the single element in the response corresponds to Customer A (`$[0].customerName`).
    *   Verify the aggregated values for Customer A are correct (`$[0].totalOrders` is 5, `$[0].totalSpent` is 1200).

---

## Production Ready Checklist ✅
- [ ] Test data is carefully crafted to cover both positive and negative cases for the report logic.
- [ ] The integration test validates the behavior of a complex `@Query` with aggregations.
- [ ] The test confirms that a custom DTO projection is correctly serialized into JSON.
- [ ] The full API flow (Controller -> Service -> Repository -> Database) is tested.
