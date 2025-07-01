-- DRINK ORDERS DB-- 
CREATE DATABASE IF NOT EXISTS drink_orders;
USE drink_orders;


CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(20) NOT NULL,
    drink_name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    branch_name VARCHAR(50) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    ordervalue DECIMAL(10, 2) NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS branches (
    branch_id INT PRIMARY KEY,
    branch_name VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    manager VARCHAR(100),
    contact VARCHAR(20)
);


INSERT INTO branches (branch_id, branch_name, location) VALUES
(1, 'HQ Nairobi', 'Nairobi CBD'),
(2, 'Nakuru', 'Nakuru Town'),
(3, 'Mombasa', 'Mombasa Island'),
(4, 'Kisumu', 'Kisumu City');


CREATE TABLE IF NOT EXISTS drinks (
    drink_id INT AUTO_INCREMENT PRIMARY KEY,
    drink_name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);


INSERT INTO drinks (drink_name, price) VALUES
('Coke', 50.00),
('Fanta', 45.00),
('Sprite', 45.00),
('Pepsi', 50.00),
('Water', 30.00);


CREATE TABLE IF NOT EXISTS stock (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    drink_name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id)
);


INSERT INTO stock (branch_id, drink_name, quantity) VALUES
(1, 'Coke', 50),
(1, 'Fanta', 30),
(2, 'Sprite', 20),
(2, 'Pepsi', 25),
(3, 'Coke', 15),
(3, 'Krest', 10),
(4, 'Fanta', 40),
(4, 'Pepsi', 35);