-- Seed data for JPA additional fields lifecycle test
DROP TABLE IF EXISTS lifecycle_test;

CREATE TABLE lifecycle_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

INSERT INTO lifecycle_test (id, name, description, password, price, video_url)
VALUES (1, 'Lifecycle Test Entity', 'This is a test entity for lifecycle testing.', 'testPassword123', 99.99, NULL);
