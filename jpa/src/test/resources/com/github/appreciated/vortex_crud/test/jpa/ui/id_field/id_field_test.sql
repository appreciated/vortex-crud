DROP TABLE IF EXISTS field_types_test_tags;
DROP TABLE IF EXISTS field_types_test;

CREATE TABLE field_types_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

INSERT INTO field_types_test (id, name, pdf_doc, notes) VALUES (1, 'Test Entity 1', 'test.pdf', '## Header');
