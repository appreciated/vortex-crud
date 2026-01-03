DELETE
FROM datetime_validation_test;

-- Seed data for JPA field validation tests
-- SQLite JDBC requires full timestamp format even for LocalDate fields
INSERT INTO datetime_validation_test (id, required_field, email_field, numeric_field, date_field, datetime_field, enum_field, checkbox_field, image_field)
VALUES (1, 'Test Value', 'test@example.com', 42, '2023-01-01 00:00:00.000', '2023-01-01 10:15:00.000', 'OPTION1', 1, './red.png'),
       (2, 'Another Value', 'user@domain.org', 100, '2023-02-15 00:00:00.000', '2023-02-15 11:20:00.000', 'OPTION2', 0, './green.png');