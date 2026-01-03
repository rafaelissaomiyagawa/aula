# Exercise 13: Integration Testing "Product Consulting APIs"

## Context
The frontend needs to reliably display product information, including search results, paginated listings, and top products. We need integration tests to ensure these product consulting APIs work correctly with the database, handling various query parameters and returning accurate DTOs.

## Objective
Write integration tests for `GET` endpoints using `MockMvc` and a real database managed by TestContainers. You will validate HTTP status codes, JSON response bodies, pagination metadata, and sorting logic.

---

### Part 1: Test Product Search by Category

**Feature Request:** "As a user, I want to filter products by their category so I can easily find what I'm looking for."

**Task:**
1.  Create a new test class `ProductControllerIntegrationTest` that extends `BaseIntegrationTest`.
2.  Annotate it with `@AutoConfigureMockMvc`.
3.  Write a test method: `shouldReturnProductsWhenSearchingByCategory()`.
4.  **Arrange:** Use `TestDataInitializer` or an SQL script to ensure products with different categories exist in the database.
5.  **Act:** Perform a `GET` request to `/api/products/search/category?category=BOOKS` using `MockMvc`.
6.  **Assert:**
    *   Verify the HTTP status is `200 OK`.
    *   Use `jsonPath` to assert that the returned list is not empty.
    *   Use `jsonPath` to assert that every item in the response has `$.category` equal to `"BOOKS"`.

---

### Part 2: Test Paginated Product List

**Feature Request:** "As a mobile user, I want product lists to be paginated so the app loads quickly and I can scroll through pages."

**Task:**
1.  In `ProductControllerIntegrationTest`, write a new test: `shouldReturnPaginatedAndSortedProducts()`.
2.  **Act:** Perform a `GET` request to `/api/products?page=0&size=2&sort=price,desc`.
3.  **Assert:**
    *   Verify the HTTP status is `200 OK`.
    *   Use `jsonPath` to check `$.totalElements` and `$.totalPages`.
    *   Assert that the size of the `$.content` array is `2`.
    *   Assert that the first element in the list has a higher price than the second, confirming the sort order.

---

### Part 3: Test Top Expensive Products

**Feature Request:** "As a marketer, I want to quickly see our most expensive items to feature them in a campaign."

**Task:**
1.  Write a test method: `shouldReturnTop5ExpensiveProducts()`.
2.  **Act:** Perform a `GET` request to `/api/products/search/top-expensive`.
3.  **Assert:**
    *   Verify the HTTP status is `200 OK`.
    *   Assert that the `content` array has a size of `5`.
    *   Verify that the list is sorted by price in descending order.

---

## Production Ready Checklist ✅
- [ ] API `GET` endpoints are tested against a real database.
- [ ] Test data is reliably loaded before each test run.
- [ ] Assertions are made on both HTTP status and the content of the JSON response.
- [ ] Pagination and sorting logic are explicitly verified.
