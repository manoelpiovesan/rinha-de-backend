--- Customers
-- 1
INSERT INTO customer (id, account_limit, balance)
values (1, 100000, 0);

-- 2
INSERT INTO customer (id, account_limit, balance)
values (2, 80000, 0);

-- 3
INSERT INTO customer (id, account_limit, balance)
values (3, 1000000, 0);

-- 4
INSERT INTO customer (id, account_limit, balance)
values (4, 10000000, 0);

-- 5
INSERT INTO customer (id, account_limit, balance)
values (5, 500000, 0);


-- -- Transactions
-- -- 1
-- INSERT INTO transaction (id, amount, description, type, customer_id, created_at)
-- values (1, 1000, 'Escola', 'CREDIT', 1, NOW());