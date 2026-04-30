-- =========================================================
-- Proyecto: Transaction_backend
-- Descripción: Script completo de bases de datos para
-- customer-service y account-service
-- =========================================================

-- =========================================================
-- CUSTOMER-SERVICE DATABASE
-- =========================================================

-- Crear extensión para encriptación de contraseñas
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE persons (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(120) NOT NULL,
                         gender VARCHAR(20) NOT NULL CHECK (gender IN ('MALE','FEMALE')),
                         age INTEGER NOT NULL CHECK (age > 0),
                         identification VARCHAR(30) NOT NULL UNIQUE,
                         address VARCHAR(255) NOT NULL,
                         phone VARCHAR(30) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                           id BIGINT PRIMARY KEY,
                           client_id VARCHAR(50) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           status BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_customers_persons
                               FOREIGN KEY (id)
                                   REFERENCES persons(id)
                                   ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_customers_client_id ON customers(client_id);
CREATE INDEX idx_persons_identification ON persons(identification);

-- =========================
-- DATA SEED
-- =========================

WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Jose Lema','MALE',34,'1723434343','Otavalo sn y principal','098254785')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'joselema', crypt('1234', gen_salt('bf')), true FROM p;

WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Marianela Montalvo','FEMALE',28,'1723434344','Amazonas y NNUU','097548965')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'marianela', crypt('5678', gen_salt('bf')), true FROM p;

WITH p AS (
INSERT INTO persons(name, gender, age, identification, address, phone)
VALUES ('Juan Osorio','MALE',32,'1723434345','13 junio y Equinoccial','098874587')
    RETURNING id
    )
INSERT INTO customers(id, client_id, password, status)
SELECT id, 'juanosorio', crypt('1245', gen_salt('bf')), true FROM p;

-- =========================================================
-- ACCOUNT-SERVICE DATABASE
-- =========================================================

CREATE TABLE customer_reference (
                                    id BIGSERIAL PRIMARY KEY,
                                    client_id VARCHAR(50) NOT NULL UNIQUE,
                                    client_name VARCHAR(120) NOT NULL,
                                    status BOOLEAN NOT NULL DEFAULT TRUE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          customer_ref_id BIGINT NOT NULL,
                          account_number VARCHAR(20) NOT NULL UNIQUE,
                          account_type VARCHAR(20) NOT NULL
                              CHECK (account_type IN ('AHORRO', 'CORRIENTE')),
                          initial_balance NUMERIC(12,2) NOT NULL,
                          current_balance NUMERIC(12,2) NOT NULL,
                          status BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          ```
                          CONSTRAINT fk_accounts_customer
                              FOREIGN KEY (customer_ref_id)
                              REFERENCES customer_reference(id)
                          ```

);

CREATE TABLE movements (
                           id BIGSERIAL PRIMARY KEY,
                           account_id BIGINT NOT NULL,
                           movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           movement_type VARCHAR(20) NOT NULL
                               CHECK (movement_type IN ('DEPOSIT', 'WITHDRAW')),
                           amount NUMERIC(12,2) NOT NULL,
                           balance NUMERIC(12,2) NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           ```
                           CONSTRAINT fk_movements_account
                               FOREIGN KEY (account_id)
                               REFERENCES accounts(id)
                               ON DELETE CASCADE
                           ```

);

-- Índices
CREATE INDEX idx_accounts_number ON accounts(account_number);
CREATE INDEX idx_movements_account ON movements(account_id);
CREATE INDEX idx_customer_reference_client ON customer_reference(client_id);

-- =========================
-- DATA SEED
-- =========================

INSERT INTO customer_reference (client_id, client_name, status)
VALUES
    ('joselema', 'Jose Lema', true),
    ('marianela', 'Marianela Montalvo', true),
    ('juanosorio', 'Juan Osorio', true);

INSERT INTO accounts (customer_ref_id, account_number, account_type, initial_balance, current_balance, status)
VALUES
    (1, '478758', 'AHORRO', 2000.00, 1425.00, true),
    (2, '225487', 'CORRIENTE', 100.00, 700.00, true),
    (3, '495878', 'AHORRO', 0.00, 150.00, true),
    (2, '496825', 'AHORRO', 540.00, 0.00, true);

INSERT INTO movements (account_id, movement_type, amount, balance)
VALUES
    (1, 'WITHDRAW', 575.00, 1425.00),
    (2, 'DEPOSIT', 600.00, 700.00),
    (3, 'DEPOSIT', 150.00, 150.00),
    (4, 'WITHDRAW', 540.00, 0.00);

