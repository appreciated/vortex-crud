-- Seed data for jOOQ additional fields password test
DROP TABLE IF EXISTS password_test;

CREATE TABLE password_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

INSERT INTO password_test (id, name, description, password, price, video_url)
VALUES (1, 'Password Test Entity', 'This is a test entity for password field testing.', 'testPassword123', 99.99, NULL);
