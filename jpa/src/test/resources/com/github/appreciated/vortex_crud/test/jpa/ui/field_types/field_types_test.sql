DROP TABLE IF EXISTS field_types_test;

CREATE TABLE field_types_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    tags        VARCHAR(255),
    pdf_doc     VARCHAR(255),
    notes       TEXT,
    date_range       VARCHAR(500),
    date_time_range  VARCHAR(500),
    checkbox_field BOOLEAN,
    date_field DATE,
    datetime_field TIMESTAMP,
    double_field DOUBLE,
    email_field VARCHAR(255),
    file_field VARCHAR(255),
    image_field VARCHAR(255),
    integer_field INTEGER,
    select_field VARCHAR(255)
);

INSERT INTO field_types_test (id, name, tags, pdf_doc, notes, date_range, date_time_range, checkbox_field, date_field, datetime_field, double_field, email_field, file_field, image_field, integer_field, select_field)
VALUES (1, 'Test Entity 1', 'tag1,tag2', 'test.pdf', '## Header', '2023-01-01,2023-01-31', '2023-01-01T10:00:00,2023-01-31T18:00:00', true, '2023-05-20', '2023-05-20 12:00:00', 10.5, 'test@example.com', 'test.txt', 'test.png', 42, 'option1');
