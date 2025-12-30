-- Database Creation
-- CREATE DATABASE stock_portfolio_db;

-- Connect to the database before running the following:

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Stocks Table (Master Data)
CREATE TABLE IF NOT EXISTS stocks (
    id SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL
);

-- Portfolio Table
CREATE TABLE IF NOT EXISTS portfolio (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    stock_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    buy_price DECIMAL(10, 2) NOT NULL,
    buy_date DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (stock_id) REFERENCES stocks(id) ON DELETE CASCADE
);

-- Sample Data (Optional)
-- INSERT INTO stocks (symbol, company_name) VALUES ('AAPL', 'Apple Inc.');
-- INSERT INTO stocks (symbol, company_name) VALUES ('GOOGL', 'Alphabet Inc.');
-- INSERT INTO stocks (symbol, company_name) VALUES ('MSFT', 'Microsoft Corp.');
