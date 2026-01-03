# Exercise 8: The Order Status Checker (REST API & Error Handling)

## Context
Customers are calling support just to ask for the status of their order. This is time-consuming for the support team and a poor experience for the customer. We need to create a secure, read-only API endpoint that allows customers to check their order status themselves.

## Objective
Build a RESTful `GET` endpoint that retrieves an `Order` by its public `orderNumber`, returns a clean DTO, and handles the "not found" case gracefully with a custom exception and a 404 response.

---

## Part 1: Finding the Order

### Task
1.  Open the `OrderRepository` interface.
2.  Verify that a method `Optional<Order> findByOrderNumber(String orderNumber)` exists. This allows us to find an order by its public, non-sequential ID.

---

## Part 2: Custom Exception for "Not Found"

### Task
1.  Create a new exception class `ResourceNotFoundException` in the `exception` package.
2.  Annotate this class with `@ResponseStatus(HttpStatus.NOT_FOUND)`. This tells Spring to automatically return a 404 status code whenever this exception is thrown from a controller.

---

## Part 3: Service Layer Logic

### Task
1.  Open `OrderService`.
2.  Create a new method `public OrderResponse findByOrderNumber(String orderNumber)`.
3.  Inside, use the repository method. If the order is not found, throw your new `ResourceNotFoundException`.
4.  If the order *is* found, map the `Order` entity to an `OrderResponse` DTO before returning. **Never return raw entities from an API.**

---

## Part 4: The REST Controller

### Task
1.  Create a new `OrderController`.
2.  Inject the `OrderService`.
3.  Create a `GET` endpoint mapped to `/api/orders/{orderNumber}`.
4.  The method should accept the `orderNumber` as a `@PathVariable` and call the service method.

### Verification
*   Start the application.
*   Use a tool like `curl`, Postman, or `place_order.hurl` to first create an order.
*   Copy the `orderNumber` from the response.
*   Make a `GET` request to `http://localhost:8080/api/orders/{your_order_number}`. It should return the order details with a 200 OK.
*   Make a `GET` request to `http://localhost:8080/api/orders/invalid-number`. It should return a 404 Not Found.

---

## Production Ready Checklist ✅
- [ ] The API uses a non-primary-key (`orderNumber`) for public access.
- [ ] A specific `ResourceNotFoundException` is used instead of a generic one.
- [ ] The endpoint returns a clean DTO, not a JPA entity.
- [ ] Invalid inputs result in a 404, not a 500 error.
