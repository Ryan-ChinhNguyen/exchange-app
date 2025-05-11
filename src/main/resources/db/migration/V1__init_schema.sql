CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL
);

CREATE TABLE exchange_rate_log (
    id SERIAL PRIMARY KEY,
    base_currency VARCHAR(10),
    target_currency VARCHAR(10),
    rate DOUBLE PRECISION,
    timestamp TIMESTAMP
);