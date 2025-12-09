INSERT INTO aula_product (id, sku, name, price, category, stock_quantity, active)
VALUES (998, 'SQL-PROD-1', 'Product From SQL', 99.99, 'BOOKS', 50, true);

INSERT INTO aula_order (id, order_number, customer_email, order_date, status, total_amount, free_shipping, manual_review)
VALUES (999, 'SQL-ORDER-1', 'sql-user@example.com', '2025-12-08T12:00:00', 'PENDING', 199.98, false, false);
