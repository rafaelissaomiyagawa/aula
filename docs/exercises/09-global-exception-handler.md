# Exercise 9: Robust APIs with a Global Exception Handler

## Context
Currently, if a user provides invalid data (e.g., a negative stock quantity) or tries to access a resource that doesn't exist, the API might return a long, confusing stack trace. This is unprofessional and a security risk. A production-ready API must always return clean, predictable JSON error messages.

## Objective
Implement a **`@RestControllerAdvice`** to centralize exception handling. This will intercept exceptions thrown from any controller and map them to consistent, user-friendly HTTP responses.

---

## Part 1: The "Not Found" Handler

### Task
1.  Create a class `GlobalExceptionHandler` in the `exception` package. Annotate it with `@RestControllerAdvice`.
2.  Create a custom exception `ResourceNotFoundException` if it doesn't already exist.
3.  Implement a method that handles `ResourceNotFoundException`. It should:
    *   Be annotated with `@ExceptionHandler(ResourceNotFoundException.class)`.
    *   Return a `ResponseEntity<Map<String, String>>` with a `404 NOT_FOUND` status.
    *   The JSON body should be simple, e.g., `{"error": "The requested resource was not found."}`.

### Verification
*   Call `GET /api/orders/invalid-order-number`.
*   The response should be a `404` with your clean JSON message, not a generic Spring error page.

---

## Part 2: The "Validation" Handler

### Task
1.  In `GlobalExceptionHandler`, add a handler for `MethodArgumentNotValidException`. This exception is automatically thrown by Spring when an object annotated with `@Valid` fails validation (e.g., `@Min(0)`).
2.  The handler should return a `400 BAD_REQUEST`.
3.  The body should be more complex. Iterate through the validation errors (`ex.getBindingResult().getFieldErrors()`) to build a map of invalid fields and their error messages, e.g., `{"quantity": "must be greater than or equal to 0"}`.

### Verification
*   Use `curl` or Postman to send a `PATCH` request to `/api/products/{id}/stock` with a negative quantity.
*   The response should be a `400` with a JSON body detailing which field was invalid and why.

---

## Part 3: The "Catch-All" Handler (Safety Net)

### Task
1.  Add a final handler for the generic `Exception.class`. This acts as a safety net for any unexpected errors.
2.  **Crucially**, this handler should log the full exception (`log.error("Unhandled exception:", ex)`).
3.  It should return a `500 INTERNAL_SERVER_ERROR` with a generic, non-disclosing message like `{"error": "An unexpected internal server error occurred."}`. **Never leak stack trace details to the user.**

### Verification
*   This one is harder to test, but you can be confident it will catch any unhandled `NullPointerException` or other runtime errors and prevent a full stack trace from being sent to the client.

---

## Production Ready Checklist ✅
- [ ] Invalid resource IDs return a clean 404.
- [ ] Validation failures return a clean 400 with field-specific details.
- [ ] Unexpected server errors return a generic 500 and are logged internally.
- [ ] No controller contains `try/catch` blocks for handled exceptions.
