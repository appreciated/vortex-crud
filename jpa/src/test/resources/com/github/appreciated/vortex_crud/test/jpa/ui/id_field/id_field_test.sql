DROP TABLE IF EXISTS id_field_test_tags;
DROP TABLE IF EXISTS id_field_test;

CREATE TABLE id_field_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

INSERT INTO id_field_test (id, name, pdf_doc, notes) VALUES (1, 'Test Entity 1', 'test.pdf', '## Header');
