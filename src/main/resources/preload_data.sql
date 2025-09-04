INSERT INTO customer (id, name, mobile_number, email_id, customer_id)
VALUES (1, 'Narmada', '9876543210', 'narmada@example.com', 'CUST0001');

INSERT INTO purchase (id, customer_id, amount, purchase_date, reward_points)
VALUES
  (1, 'CUST0001', 120.0, '2025-07-15', 90),
  (2, 'CUST0001', 100.0, '2025-08-10', 50),
  (3, 'CUST0001', 110.0, '2025-09-01', 90);
  (4, 'CUST0001', 130.0, '2025-09-03', 110);
