-- Seed data for JOOQ master-detail tests
CREATE TABLE master_detail_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

DROP TABLE IF EXISTS master_detail_tasks;

CREATE TABLE master_detail_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES  master_detail_users (id)
);

INSERT INTO master_detail_tasks (id, title, description, status) VALUES
  (1, 'Done Task A', 'Finished task', 'Done'),
  (2, 'Done Task B', 'Another finished task', 'Done');
