-- One-to-many relationship test for JPA
CREATE TABLE IF NOT EXISTS jpa_departments (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS jpa_employees (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    department_id INTEGER,
    FOREIGN KEY (department_id) REFERENCES jpa_departments(id)
);

-- Insert test data
INSERT INTO jpa_departments (id, name, location)
VALUES 
    (1, 'Engineering', 'Building A'),
    (2, 'Marketing', 'Building B'),
    (3, 'Human Resources', 'Building C');

INSERT INTO jpa_employees (id, name, email, department_id)
VALUES 
    (1, 'John Smith', 'john@example.com', 1),
    (2, 'Jane Doe', 'jane@example.com', 1),
    (3, 'Bob Johnson', 'bob@example.com', 1),
    (4, 'Alice Brown', 'alice@example.com', 2),
    (5, 'Charlie Davis', 'charlie@example.com', 2),
    (6, 'Eva Wilson', 'eva@example.com', 3);