-- Seed data for jOOQ additional fields lifecycle test
DELETE FROM additional_fields_test;

INSERT INTO additional_fields_test (id, name, description, password, price, video_url)
VALUES (1, 'Lifecycle Test Entity', 'This is a test entity for lifecycle testing.', 'testPassword123', 99.99, NULL);
