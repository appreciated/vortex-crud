-- Nested forms test for JPA
CREATE TABLE IF NOT EXISTS jpa_orders (
    id INTEGER PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255),
    order_date DATE DEFAULT CURRENT_DATE,
    shipping_address TEXT,
    billing_address TEXT,
    total_amount DECIMAL(10, 2),
    status VARCHAR(50) CHECK (status IN ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);

CREATE TABLE IF NOT EXISTS jpa_order_items (
    id INTEGER PRIMARY KEY,
    order_id INTEGER,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER CHECK (quantity > 0),
    unit_price DECIMAL(10, 2),
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES jpa_orders(id)
);

CREATE TABLE IF NOT EXISTS jpa_order_notes (
    id INTEGER PRIMARY KEY,
    order_id INTEGER,
    note_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES jpa_orders(id)
);

-- Insert test data
INSERT INTO jpa_orders (
    id, customer_name, customer_email, order_date, 
    shipping_address, billing_address, total_amount, status
)
VALUES 
    (
        1, 
        'John Smith', 
        'john@example.com', 
        '2023-07-10', 
        '123 Main St, Apt 4B, Anytown, ST 12345', 
        '123 Main St, Apt 4B, Anytown, ST 12345', 
        159.97, 
        'PROCESSING'
    ),
    (
        2, 
        'Jane Doe', 
        'jane@example.com', 
        '2023-07-15', 
        '456 Oak Ave, Somewhere, ST 67890', 
        '789 Pine St, Elsewhere, ST 54321', 
        249.95, 
        'PENDING'
    );

INSERT INTO jpa_order_items (
    id, order_id, product_name, quantity, unit_price, subtotal
)
VALUES 
    (1, 1, 'Smartphone Case', 1, 29.99, 29.99),
    (2, 1, 'Wireless Headphones', 1, 79.99, 79.99),
    (3, 1, 'Screen Protector', 2, 24.99, 49.98),
    (4, 2, 'Laptop', 1, 199.99, 199.99),
    (5, 2, 'Mouse', 1, 24.99, 24.99),
    (6, 2, 'Keyboard', 1, 24.97, 24.97);

INSERT INTO jpa_order_notes (
    id, order_id, note_text, created_at
)
VALUES 
    (1, 1, 'Customer requested express shipping', '2023-07-10 10:30:00'),
    (2, 1, 'Package prepared for shipping', '2023-07-11 14:15:00'),
    (3, 2, 'Customer may change shipping address', '2023-07-15 09:45:00');