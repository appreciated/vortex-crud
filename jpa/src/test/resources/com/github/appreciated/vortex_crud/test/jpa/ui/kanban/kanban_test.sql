-- Seed data for JOOQ kanban tests
DROP TABLE IF EXISTS kanban_users;

CREATE TABLE kanban_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

DROP TABLE IF EXISTS kanban_tasks;

CREATE TABLE kanban_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    row_index   INTEGER,
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES kanban_users (id)
);

INSERT INTO kanban_tasks (id, title, description, status, row_index) VALUES
  (1, 'Task A', 'First task', 'a', 0),
  (2, 'Task B', 'Second task', 'a', 1),
  (3, 'Task C', 'Third task', 'b', 0);
