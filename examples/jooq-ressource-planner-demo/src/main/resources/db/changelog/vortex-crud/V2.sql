-- liquibase formatted sql

-- changeset resource-planner-demo:4
CREATE TABLE customer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Migrate existing customer data from appointments to customer table
INSERT INTO customer (name, email)
SELECT DISTINCT customer_name, customer_email
FROM appointment
WHERE customer_name IS NOT NULL;

ALTER TABLE appointment ADD COLUMN customer_id INTEGER REFERENCES customer(id);
ALTER TABLE appointment ADD COLUMN recurrence_frequency VARCHAR(20) DEFAULT 'NONE'; -- NONE, DAILY, WEEKLY, MONTHLY, YEARLY
ALTER TABLE appointment ADD COLUMN recurrence_interval INTEGER DEFAULT 1;
ALTER TABLE appointment ADD COLUMN recurrence_end_date TIMESTAMP;
ALTER TABLE appointment ADD COLUMN recurrence_group_id INTEGER; -- To identify the series

-- Update appointment with customer_id
UPDATE appointment
SET customer_id = (SELECT id FROM customer WHERE customer.name = appointment.customer_name);

CREATE INDEX idx_appointment_customer ON appointment(customer_id);

-- Note: Dropping columns in SQLite requires table rebuild or just ignore them.
-- Since this is an enhancement on a demo, we will leave them for now or assume they are deprecated.
-- If strict cleanup is needed, we would need to recreate the table.
