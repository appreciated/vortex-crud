DROP TABLE IF EXISTS subroute_tasks;
CREATE TABLE subroute_tasks (
    id INT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(1000),
    assigned_to INT,
    status VARCHAR(50),
    due_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

INSERT INTO subroute_tasks (id, title, status) VALUES (1, 'Initial Value', 'OPEN');
