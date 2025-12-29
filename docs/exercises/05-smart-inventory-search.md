# Exercise 5: The "Smart Inventory" Search (Derived Queries)

## Context
The warehouse team is struggling to find products efficiently. They need a simple API that can filter products by category, find items within a price range, and search for names using partial matches (like a search bar).

## Objective
Implement a robust search capability using **Spring Data JPA Derived Query Methods**. You will create powerful database queries simply by defining method names in your repository interface, without writing any SQL.

---

## Part 1: Category Filtering

### Task
1. Open `ProductRepository`.
2. Create a method that finds all products belonging to a specific `ProductCategory`.
3. The method should return a `List<Product>`.

### Verification
*   Method name should follow the pattern `findBy...`.
*   It should accept a `ProductCategory` enum as a parameter.

---

## Part 2: Price Range Search

### Task
1. Add a method to `ProductRepository` to find products within a specific price range.
2. It should accept two `BigDecimal` parameters: `minPrice` and `maxPrice`.

### Verification
*   How do you express "Between" in a method name?

---

## Part 3: Name Search (Partial Match)

### Task
1. Add a method to search for products where the name **contains** a specific string.
2. Ensure the search is **case-insensitive** (e.g., "phone" should match "Smartphone").

### Verification
*   Use keywords like `Containing` and `IgnoreCase`.

---

## Part 4: Top Expensive Products

### Task
1. Create a method that finds the **Top 5** most expensive products in the catalog.
2. You do not need to pass any parameters.

### Verification
*   Combine `Top`, `OrderBy`, and `Desc` keywords.

---

## Part 5: The Search API (Controller)

### Task
1. Create a `ProductController` in the `controller` package.
2. Expose endpoints for each search capability:
    *   `GET /api/products/search/category?category=ELECTRONICS`
    *   `GET /api/products/search/price?min=10&max=100`
    *   `GET /api/products/search/name?name=phone`
    *   `GET /api/products/search/top-expensive`

---

## Production Ready Checklist ✅
- [ ] No manual `@Query` annotations used (let Spring Data generate the SQL).
- [ ] All methods cover the business requirements.
- [ ] Method names are descriptive and follow the convention.
