-- Seed data for jooq additional fields tests
DELETE FROM additional_fields_test;

INSERT INTO additional_fields_test (id, name, description, password, price, video_url)
VALUES (1, 'Test Entity', 'This is a long description that spans multiple lines and demonstrates the TextArea field component.', 'testPassword123', 99.99, './sample.mp4');
