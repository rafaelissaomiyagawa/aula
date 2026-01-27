POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customerEmail": "customer@example.com",
  "isVip": true,
  "items": [
    {
      "productId": 2,
      "quantity": 2
    }
    ]
}
