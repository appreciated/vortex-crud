-- Seed data for JPA text field tests
INSERT INTO text_field_test (id, required_field, email_field, numeric_field, date_field, datetime_field, enum_field, checkbox_field, image_field)
VALUES (1, 'Test Value', 'test@example.com', 42, '2023-01-01', '2023-01-01 10:15:00', 'OPTION1', true, './red.png'),
       (2, 'Another Value', 'user@domain.org', 100, '2023-02-15', '2023-02-15 11:20:00', 'OPTION2', false, './green.png');
