-- Field validation test for JPA
DROP TABLE IF EXISTS jpa_validation_test;

CREATE TABLE jpa_validation_test (
                                                   id INTEGER PRIMARY KEY,
                                                   required_field VARCHAR(255) NOT NULL,
                                                   email_field VARCHAR(255) CHECK (email_field LIKE '%@%.%'),
                                                   numeric_field REAL CHECK (numeric_field > 0),
                                                   date_field DATE,
                                                   enum_field VARCHAR(20) CHECK (enum_field IN ('OPTION1', 'OPTION2', 'OPTION3')),
                                                   image_field VARCHAR(255)
);

INSERT INTO jpa_validation_test (id, required_field, email_field, numeric_field, date_field, enum_field, image_field)
VALUES
    (1, 'Test Value', 'test@example.com', 42, '2023-01-01 00:00:00.000', 'OPTION1', './images/red.png'),
    (2, 'Another Value', 'user@domain.org', 100, '2023-02-15 00:00:00.000', 'OPTION2', './images/green.png');
