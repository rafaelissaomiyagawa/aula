# Business Rules from com.example.junit

This document outlines the business rules extracted from the `com.example.junit` package, with references to their locations in the code.

### Order Placement (`InventoryService#placeOrder`)
- A product must exist to be added to an order.
- There must be enough stock for a product to be added to an order.
- A maximum of 3 units of an electronic product can be ordered at a time.
- A 10% discount is applied to non-electronic products when 5 or more units are ordered.
- Orders over $100 qualify for free shipping.
- Orders over $500 require manual review.

### Order Cancellation (`InventoryService#cancelOrder`)
- An order cannot be canceled if it has already been shipped or delivered.
- When an order is canceled, the stock of the products in the order is replenished.

### Refunds (`InventoryService#processRefund`)
- Grocery items cannot be returned after 2 days from the order date.
- A 20% restocking fee is applied to refunds for electronic products.
- When an item is refunded, its stock is replenished.

### Stock Management (`InventoryService#updateStock`)
- Stock quantity cannot be negative.

### Data Validation

- **SKU:** Must follow the format `ABC-12345`.
    - _Location:_ `SkuValidator#isValid`

- **Order:**
    - `orderNumber` is unique and not empty.
        - _Location:_ `@Column(unique = true, nullable = false)` on `orderNumber` field in `Order` class.
    - `customerEmail` must be a valid email format and not empty.
        - _Location:_ `@Email` and `@NotEmpty` on `customerEmail` field in `Order` class.
    - `orderDate`, `status`, and `totalAmount` are mandatory.
        - _Location:_ `@Column(nullable = false)` and `@NotNull` on the respective fields in `Order` class.

- **OrderItem:**
    - `quantity` must be at least 1.
        - _Location:_ `@Min(1)` on `quantity` field in `OrderItem` class.
    - `order`, `product`, and `unitPrice` are mandatory.
        - _Location:_ `@NotNull` on the respective fields in `OrderItem` class.

- **Product:**
    - `sku` is unique, not empty, and follows the SKU format.
        - _Location:_ `@Column(unique = true, nullable = false)`, `@NotEmpty`, and `@Sku` on `sku` field in `Product` class.
    - `name` is not empty.
        - _Location:_ `@NotEmpty` on `name` field in `Product` class.
    - `price` must be at least 0.01.
        - _Location:_ `@DecimalMin("0.01")` on `price` field in `Product` class.
    - `category` and `stockQuantity` are mandatory.
        - _Location:_ `@NotNull` and `@Min(0)` on the respective fields in `Product` class.
    - `stockQuantity` cannot be negative.
        - _Location:_ `@Min(0)` on `stockQuantity` field in `Product` class.