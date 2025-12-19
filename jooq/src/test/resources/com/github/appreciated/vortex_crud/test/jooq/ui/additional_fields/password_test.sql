-- Seed data for jOOQ additional fields password test
DELETE FROM additional_fields_test;

INSERT INTO additional_fields_test (id, name, description, password, price, video_url)
VALUES (1, 'Password Test Entity', 'This is a test entity for password field testing.', 'testPassword123', 99.99, NULL);
