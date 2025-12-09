POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerEmail": "customer@example.com",
    "items": [
    {
      "productId": 102,
      "quantity": 1
    }
    ]
}
