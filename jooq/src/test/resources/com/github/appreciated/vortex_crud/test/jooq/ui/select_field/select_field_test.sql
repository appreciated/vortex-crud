DROP TABLE IF EXISTS select_field_test;

CREATE TABLE select_field_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

INSERT INTO select_field_test (id, name, pdf_doc, notes) VALUES (1, 'Option 1', 'test.pdf', '## Header');
