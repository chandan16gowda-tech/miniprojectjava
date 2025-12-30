DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS portfolio;
DROP TABLE IF EXISTS stocks;
-- Dropping users as we are simplifying to single-user academic demo
DROP TABLE IF EXISTS users; 

-- MODULE 1: Stock Management (Market Data)
CREATE TABLE stocks (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    current_price DECIMAL(10, 2) NOT NULL
);

-- MODULE 2: Transaction Management (History)
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) REFERENCES stocks(symbol),
    type VARCHAR(10) NOT NULL, -- 'BUY' or 'SELL'
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MODULE 3: Portfolio (Holdings Snapshot)
CREATE TABLE portfolio (
    symbol VARCHAR(10) PRIMARY KEY REFERENCES stocks(symbol),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    avg_buy_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);

-- Sample Data
INSERT INTO stocks (symbol, name, current_price) VALUES ('AAPL', 'Apple Inc.', 150.00);
INSERT INTO stocks (symbol, name, current_price) VALUES ('GOOGL', 'Alphabet Inc.', 2800.00);
INSERT INTO stocks (symbol, name, current_price) VALUES ('TSLA', 'Tesla Inc.', 700.00);
