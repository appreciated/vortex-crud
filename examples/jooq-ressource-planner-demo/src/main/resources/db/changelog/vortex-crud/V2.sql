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

ALTER TABLE appointment ADD COLUMN customer_id INTEGER REFERENCES customer(id);
ALTER TABLE appointment ADD COLUMN recurrence_frequency VARCHAR(20) DEFAULT 'NONE'; -- NONE, DAILY, WEEKLY, MONTHLY, YEARLY
ALTER TABLE appointment ADD COLUMN recurrence_interval INTEGER DEFAULT 1;
ALTER TABLE appointment ADD COLUMN recurrence_end_date TIMESTAMP;
ALTER TABLE appointment ADD COLUMN recurrence_group_id INTEGER; -- To identify the series

CREATE INDEX idx_appointment_customer ON appointment(customer_id);
