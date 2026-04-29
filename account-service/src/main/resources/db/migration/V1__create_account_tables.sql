INSERT INTO customer_reference (client_id, client_name, status)
VALUES
    ('joselema', 'Jose Lema', true),
    ('marianela', 'Marianela Montalvo', true),
    ('juanosorio', 'Juan Osorio', true);

INSERT INTO accounts
(customer_ref_id, account_number, account_type, initial_balance, current_balance, status)
VALUES
    (1, '478758', 'AHORRO', 2000.00, 1425.00, true),
    (2, '225487', 'CORRIENTE', 100.00, 700.00, true),
    (3, '495878', 'AHORRO', 0.00, 150.00, true),
    (2, '496825', 'AHORRO', 540.00, 0.00, true);

INSERT INTO movements
(account_id, movement_type, amount, balance)
VALUES
    (1, 'WITHDRAW', 575.00, 1425.00),
    (2, 'DEPOSIT', 600.00, 700.00),
    (3, 'DEPOSIT', 150.00, 150.00),
    (4, 'WITHDRAW', 540.00, 0.00);