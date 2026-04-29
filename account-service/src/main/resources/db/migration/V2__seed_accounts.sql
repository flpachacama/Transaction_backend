-- Seed inicial para account-service
-- Compatible con PostgreSQL + Flyway

-- 1) Referencia de clientes
-- Soporta ambos nombres de tabla por compatibilidad: customer_reference o customer_info.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'customer_reference'
    ) THEN
        INSERT INTO customer_reference (client_id, name, status)
        VALUES
            ('joselema', 'Jose Lema', TRUE),
            ('mari', 'Marianela Montalvo', TRUE),
            ('juanito', 'Juan Osorio', TRUE)
        ON CONFLICT (client_id) DO NOTHING;
    ELSIF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
          AND table_name = 'customer_info'
    ) THEN
        INSERT INTO customer_info (client_id, name, status)
        VALUES
            ('joselema', 'Jose Lema', TRUE),
            ('mari', 'Marianela Montalvo', TRUE),
            ('juanito', 'Juan Osorio', TRUE)
        ON CONFLICT (client_id) DO NOTHING;
    END IF;
END $$;

-- 2) Cuentas iniciales
INSERT INTO accounts (
    account_number,
    account_type,
    initial_balance,
    current_balance,
    status,
    client_id,
    client_name
)
VALUES
    ('478758', 'AHORRO', 2000.00, 1425.00, TRUE, 'joselema', 'Jose Lema'),
    ('225487', 'CORRIENTE', 100.00, 700.00, TRUE, 'mari', 'Marianela Montalvo'),
    ('495878', 'AHORRO', 0.00, 150.00, TRUE, 'juanito', 'Juan Osorio'),
    ('496825', 'AHORRO', 540.00, 0.00, TRUE, 'mari', 'Marianela Montalvo')
ON CONFLICT (account_number) DO NOTHING;

-- 3) Movimientos historicos
-- Se toma la cuenta por account_number para obtener account_id.
INSERT INTO movements (date, movement_type, amount, balance, account_id)
SELECT TIMESTAMP '2022-02-01 10:00:00', 'WITHDRAW', -575.00, 1425.00, a.id
FROM accounts a
WHERE a.account_number = '478758'
  AND NOT EXISTS (
      SELECT 1 FROM movements m
      WHERE m.account_id = a.id
        AND m.movement_type = 'WITHDRAW'
        AND m.amount = -575.00
        AND m.balance = 1425.00
  );

INSERT INTO movements (date, movement_type, amount, balance, account_id)
SELECT TIMESTAMP '2022-02-02 10:00:00', 'DEPOSIT', 600.00, 700.00, a.id
FROM accounts a
WHERE a.account_number = '225487'
  AND NOT EXISTS (
      SELECT 1 FROM movements m
      WHERE m.account_id = a.id
        AND m.movement_type = 'DEPOSIT'
        AND m.amount = 600.00
        AND m.balance = 700.00
  );

INSERT INTO movements (date, movement_type, amount, balance, account_id)
SELECT TIMESTAMP '2022-02-03 10:00:00', 'DEPOSIT', 150.00, 150.00, a.id
FROM accounts a
WHERE a.account_number = '495878'
  AND NOT EXISTS (
      SELECT 1 FROM movements m
      WHERE m.account_id = a.id
        AND m.movement_type = 'DEPOSIT'
        AND m.amount = 150.00
        AND m.balance = 150.00
  );

INSERT INTO movements (date, movement_type, amount, balance, account_id)
SELECT TIMESTAMP '2022-02-04 10:00:00', 'WITHDRAW', -540.00, 0.00, a.id
FROM accounts a
WHERE a.account_number = '496825'
  AND NOT EXISTS (
      SELECT 1 FROM movements m
      WHERE m.account_id = a.id
        AND m.movement_type = 'WITHDRAW'
        AND m.amount = -540.00
        AND m.balance = 0.00
  );
