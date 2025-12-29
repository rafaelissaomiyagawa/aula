# Exercise 7: The "VIP Customer Dashboard" (Aggregations & Projections)

## Context
Marketing needs a high-performance report to identify "VIP Customers"—those who have placed more than 3 orders and spent over a certain amount. We need a specific Data Transfer Object (DTO) that only contains the Customer Name, Total Spent, and Last Order Date, avoiding the overhead of loading full Entity graphs.

## Objective
Master **JPQL Aggregations** (`SUM`, `COUNT`, `MAX`) and **DTO Projections** (Constructor Expression) to write efficient "Reporting" queries.

---

## Part 1: The Projection (DTO)

### Task
1. Create a Java Record (or Class) named `VipCustomerDTO`.
2. Fields required:
    *   `customerName` (String)
    *   `totalOrders` (Long)
    *   `totalSpent` (BigDecimal)
    *   `lastOrderDate` (LocalDateTime)

---

## Part 2: The Complex Query

### Task
1. Open `OrderRepository`.
2. Write a custom `@Query` using JPQL.
3. The query needs to:
    *   Select from `Order o`.
    *   Group by `o.customerName`.
    *   Calculate `COUNT(o)` for total orders.
    *   Calculate `SUM(o.totalAmount)` for total spent.
    *   Calculate `MAX(o.orderDate)` for the last order.
    *   Filter using `HAVING` clause: Count > 3 AND Sum > 1000.

---

## Part 3: Constructor Mapping

### Task
1. Modify the `@Query` to return `new com.yourpackage.VipCustomerDTO(...)`.
2. This allows Hibernate to skip managing Entities and just pump data directly into your Report object. Very fast!

### Verification
Create a test that inserts 5 orders for "Alice" (total > $1000) and 1 order for "Bob". Calling the method should return only "Alice" in the list.

---

## Production Ready Checklist ✅
- [ ] No `List<Object[]>` returned; strictly typed DTOs.
- [ ] Aggregation happens in the Database, not in Java loop.
- [ ] Query handles the `GROUP BY` logic correctly.
