-- liquibase formatted sql

-- changeset resource-planner-demo:4
CREATE TABLE customer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

ALTER TABLE appointment ADD COLUMN customer_id INTEGER;
ALTER TABLE appointment ADD COLUMN recurrence_rule VARCHAR(255);
ALTER TABLE appointment ADD COLUMN parent_appointment_id INTEGER; -- For linking occurrences if expanded, or just to track series

-- Migrate existing data (sqlite specific)
INSERT INTO customer (name, email)
SELECT DISTINCT customer_name, customer_email FROM appointment WHERE customer_name IS NOT NULL;

UPDATE appointment
SET customer_id = (SELECT id FROM customer WHERE customer.name = appointment.customer_name AND (customer.email = appointment.customer_email OR (customer.email IS NULL AND appointment.customer_email IS NULL)));

-- Add foreign keys (SQLite limitations might require recreation, but let's try ALTER if supported or just leave it as logic FK for now as SQLite support for ADD CONSTRAINT is limited)
-- SQLite doesn't support ADD CONSTRAINT FOREIGN KEY via ALTER TABLE standardly in older versions, but let's try to be compliant or just rely on app logic for this demo step if complex.
-- Actually, let's just index it.
CREATE INDEX idx_appointment_customer ON appointment(customer_id);

-- changeset resource-planner-demo:5
-- Drop old columns? SQLite doesn't support DROP COLUMN easily before very recent versions.
-- We will ignore them in the application.
