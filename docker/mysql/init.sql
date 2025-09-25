-- ERP System MySQL Initialization Script
-- Creates schema, seed data, indexes, and audit logging

-- Ensure database exists
CREATE DATABASE IF NOT EXISTS erp_system
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE erp_system;

-- Users and Roles
CREATE TABLE IF NOT EXISTS roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inventory
CREATE TABLE IF NOT EXISTS categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_code VARCHAR(50) NOT NULL UNIQUE,
  name VARCHAR(150) NOT NULL,
  description VARCHAR(500),
  category_id BIGINT,
  price DECIMAL(10,2) NOT NULL DEFAULT 0,
  cost DECIMAL(10,2) NOT NULL DEFAULT 0,
  quantity_in_stock INT NOT NULL DEFAULT 0,
  reorder_level INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Customers
CREATE TABLE IF NOT EXISTS customers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_code VARCHAR(50) NOT NULL UNIQUE,
  company_name VARCHAR(150) NOT NULL,
  contact_person VARCHAR(120),
  email VARCHAR(150),
  phone VARCHAR(50),
  address VARCHAR(255),
  city VARCHAR(120),
  state VARCHAR(80),
  country VARCHAR(80)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Orders
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_number VARCHAR(50) NOT NULL UNIQUE,
  customer_id BIGINT NOT NULL,
  order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status ENUM('PENDING','PAID','SHIPPED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  total DECIMAL(12,2) NOT NULL DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seed basic data
INSERT IGNORE INTO roles (id, name, description) VALUES
  (1, 'ADMIN', 'System administrator'),
  (2, 'USER', 'Standard user');

INSERT IGNORE INTO users (id, username, email, password_hash, is_active)
VALUES (1, 'admin', 'admin@example.com', '$2a$10$abcdefghijklmnopqrstuv', 1);

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1,1);

INSERT IGNORE INTO categories (id, name) VALUES (1, 'Office Supplies'), (2, 'Electronics');

INSERT IGNORE INTO products (product_code, name, description, category_id, price, cost, quantity_in_stock, reorder_level)
VALUES ('PEN001', 'Blue Ballpoint Pen', 'Professional ballpoint pen', 1, 5.00, 3.00, 500, 100);

-- Sample customers
INSERT IGNORE INTO customers (customer_code, company_name, contact_person, email, phone, address, city, state, country) VALUES
('CUST001', 'Tech Solutions Ltd', 'John Silva', 'john@techsolutions.com', '+55 11 9999-8888', 'Rua das Flores, 123', 'São Paulo', 'SP', 'Brazil'),
('CUST002', 'Business Corp', 'Maria Santos', 'maria@businesscorp.com', '+55 21 8888-7777', 'Av. Copacabana, 456', 'Rio de Janeiro', 'RJ', 'Brazil');

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_orders_customer_date ON orders (customer_id, order_date DESC);
CREATE INDEX IF NOT EXISTS idx_products_category_stock ON products (category_id, quantity_in_stock);
CREATE INDEX IF NOT EXISTS idx_users_active_created ON users (is_active, created_at DESC);

-- Audit log (optional)
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  table_name VARCHAR(50) NOT NULL,
  record_id BIGINT NOT NULL,
  action ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  old_values JSON,
  new_values JSON,
  user_id BIGINT,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ip_address VARCHAR(45),
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_table_record (table_name, record_id),
  INDEX idx_timestamp (timestamp),
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verification queries
SELECT 'ERP System database initialization completed successfully' AS message;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_roles FROM roles;
SELECT COUNT(*) AS total_products FROM products;
SELECT COUNT(*) AS total_customers FROM customers;
