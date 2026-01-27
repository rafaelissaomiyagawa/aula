POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerEmail": "customer@example.com",
    "items": [
    {
      "productId": 2,
      "quantity": 1
    }
    ]
}
