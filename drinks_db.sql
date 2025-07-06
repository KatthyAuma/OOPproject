CREATE DATABASE IF NOT EXISTS drink_orders;
USE drink_orders;


CREATE TABLE IF NOT EXISTS branches (
    branch_id INT PRIMARY KEY,
    branch_name VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    manager VARCHAR(100),
    contact VARCHAR(20)
);


CREATE TABLE IF NOT EXISTS stock (
    stock_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NOT NULL,
    drink_name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id),
    UNIQUE KEY (branch_id, drink_name)
);


CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(20) NOT NULL,
    drink_name VARCHAR(50) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    branch_name VARCHAR(50) NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    ordervalue DECIMAL(10,2) NOT NULL CHECK (ordervalue > 0),
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO branches (branch_id, branch_name, location) VALUES
(1, 'HQ Nairobi', 'Nairobi CBD'),
(2, 'Nakuru', 'Nakuru Town'),
(3, 'Mombasa', 'Mombasa Island'),
(4, 'Kisumu', 'Kisumu City');

INSERT INTO stock (branch_id, drink_name, quantity) VALUES
(1, 'Coke', 2),
(1, 'Fanta', 30),
(2, 'Sprite', 4),
(2, 'Pepsi', 25),
(3, 'Coke', 15),
(3, 'Krest', 0),
(4, 'Fanta', 40),
(4, 'Pepsi', 1);

INSERT INTO orders (customer_name, contact_info, drink_name, quantity, branch_name, payment_method, ordervalue) VALUES
('Leon Ngatia', '0712345678', 'Coke', 2, 'HQ Nairobi', 'M-Pesa', 100.00),
('Ian Obino', '0723456789', 'Fanta', 3, 'Nakuru', 'Credit Card', 135.00),
('Sandra Elavaza', '0734567890', 'Sprite', 1, 'Mombasa', 'M-Pesa', 45.00);