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

CREATE INDEX idx_customers_client_id ON customers(client_id);
CREATE INDEX idx_persons_identification ON persons(identification);