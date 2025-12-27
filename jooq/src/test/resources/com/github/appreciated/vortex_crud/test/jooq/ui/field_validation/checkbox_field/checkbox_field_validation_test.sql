DROP TABLE IF EXISTS checkbox_field_validation_test;

CREATE TABLE checkbox_field_validation_test
(
    id             INTEGER      NOT NULL,
    required_field VARCHAR(255) NOT NULL,
    email_field    VARCHAR(255),
    numeric_field  DOUBLE PRECISION CHECK (numeric_field > 0),
    date_field     DATE,
    datetime_field TIMESTAMP,
    enum_field     VARCHAR(20),
    checkbox_field BOOLEAN,
    image_field    VARCHAR(255),
    PRIMARY KEY (id)
);
INSERT INTO checkbox_field_validation_test (id, required_field, email_field, numeric_field, date_field, datetime_field, enum_field, checkbox_field, image_field)
VALUES (1, 'Test Value', 'test@example.com', 42, '2023-01-01 00:00:00.000', '2023-01-01 10:15:00.000', 'OPTION1', 1, './red.png'),
       (2, 'Another Value', 'user@domain.org', 100, '2023-02-15 00:00:00.000', '2023-02-15 11:20:00.000', 'OPTION2', 0, './green.png');
