-- Seed data for JPA additional fields textarea test
DROP TABLE IF EXISTS additional_fields_test;

CREATE TABLE additional_fields_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

INSERT INTO additional_fields_test (id, name, description, password, price, video_url)
VALUES (1, 'TextArea Test Entity', 'This is a long description that spans multiple lines and demonstrates the TextArea field component.', 'testPassword123', 99.99, NULL);
