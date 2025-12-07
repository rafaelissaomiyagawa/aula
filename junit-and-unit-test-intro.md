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