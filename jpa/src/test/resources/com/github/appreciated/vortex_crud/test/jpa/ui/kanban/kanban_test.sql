-- Seed data for JPA tasks tests
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS images;

CREATE TABLE tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES users (id)
);

INSERT INTO tasks (id, title, description, status) VALUES
  (1, 'Task A', 'First task', 'a'),
  (2, 'Task B', 'Second task', 'b');
