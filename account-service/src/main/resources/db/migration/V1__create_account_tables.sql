CREATE TABLE customer_reference (
    id BIGSERIAL PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL UNIQUE,
    client_name VARCHAR(120) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts
(
    id              BIGSERIAL PRIMARY KEY,
    customer_ref_id BIGINT         NOT NULL,
    account_number  VARCHAR(20)    NOT NULL UNIQUE,
    account_type    VARCHAR(20)    NOT NULL
        CHECK (account_type IN ('AHORRO', 'CORRIENTE')),
    initial_balance NUMERIC(12, 2) NOT NULL,
    current_balance NUMERIC(12, 2) NOT NULL,
    status          BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_accounts_customer
        FOREIGN KEY (customer_ref_id)
            REFERENCES customer_reference (id)
);

CREATE TABLE movements
(
    id            BIGSERIAL PRIMARY KEY,
    account_id    BIGINT         NOT NULL,
    movement_date TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    movement_type VARCHAR(20)    NOT NULL
        CHECK (movement_type IN ('DEPOSIT', 'WITHDRAW')),
    amount        NUMERIC(12, 2) NOT NULL,
    balance       NUMERIC(12, 2) NOT NULL,
    created_at    TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_movements_account
        FOREIGN KEY (account_id)
            REFERENCES accounts (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_accounts_number ON accounts (account_number);
CREATE INDEX idx_movements_account ON movements (account_id);
CREATE INDEX idx_customer_reference_client ON customer_reference (client_id);