-- Seed data for JPA projects tests
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS projects;

CREATE TABLE users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

INSERT INTO users (id, username)
VALUES (1, 'max@mustermann.de'),
       (2, 'erika@musterfrau.de');

INSERT INTO projects (id, name, description, start_date, end_date, created_at, updated_at)
VALUES (1, 'Project Alpha', 'A high-priority project aimed at improving the internal system', '2023-01-01 00:00:00',
        '2023-12-31 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Project Beta', 'Developing a new customer-facing web application', '2023-05-15 00:00:00',
        '2024-06-30 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
