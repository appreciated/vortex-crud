-- Seed data for JPA multi-form tests
DROP TABLE IF EXISTS multi_form_test;

CREATE TABLE multi_form_test
(
    id           INTEGER PRIMARY KEY,
    profile_name VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    description  TEXT,
    age          INTEGER
);

INSERT INTO multi_form_test (id, profile_name, email, description, age)
VALUES (1, 'Max Mustermann', 'profile@example.com', 'This is a profile description', 25);
