-- Seed data for JOOQ subroute tests
DROP TABLE IF EXISTS subroute_users;

CREATE TABLE subroute_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

DROP TABLE IF EXISTS subroute_tasks;

CREATE TABLE subroute_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES subroute_users (id)
);

INSERT INTO subroute_tasks (id, title, description, status) VALUES
  (1, 'Subroute Task A', 'First task', 'Open'),
  (2, 'Subroute Task B', 'Second task', 'Done');
