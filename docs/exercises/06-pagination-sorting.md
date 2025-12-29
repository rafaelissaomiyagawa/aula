# Exercise 6: The "Infinite Scroll" Catalog (Pagination & Sorting)

## Context
Our product catalog has grown to over 10,000 items. Returning the entire list in a single JSON response is crashing the mobile app and overloading the database. We need to implement pagination and allow users to sort results (e.g., by price, cheapest first).

## Objective
Refactor the Product API to support **Pagination** and **Sorting** using Spring Data's `Pageable` interface.

---

## Part 1: Repository Support

### Task
1. Verify if `ProductRepository` can handle pagination.
2. Ensure it extends `JpaRepository` or `PagingAndSortingRepository`.
3. Locate (or add) a `findAll(Pageable pageable)` method (it usually exists by default).

---

## Part 2: Service Layer Update

### Task
1. Modify `InventoryService` (or create a `ProductService`).
2. Update the `findAllProducts` method to accept a `Pageable` object.
3. The method should return a `Page<ProductResponse>` instead of a `List<ProductResponse>`.
4. You will need to map the `Page<Product>` entity to `Page<ProductResponse>` DTO.

---

## Part 3: Controller Integration

### Task
1. Open `InventoryController`.
2. Update the endpoint (e.g., `GET /products`) to accept `Pageable pageable` as a method argument.
3. Spring Boot automatically converts query params like `?page=0&size=10&sort=price,desc` into this object.
4. Return `Page<ProductResponse>` to the client.

### Verification
Call the endpoint:
```http
GET /products?page=0&size=5&sort=price,desc
```
Verify the response includes `content` (the data) and `pageable` metadata (totalElements, totalPages, etc.).

---

## Production Ready Checklist ✅
- [ ] API no longer returns unbounded lists.
- [ ] Clients can control page size and sorting order.
- [ ] Response includes total page count for frontend UI logic.
