CREATE TABLE single_form_route_missing_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);
INSERT INTO single_form_route_missing_test (id, name, pdf_doc, notes) VALUES (1, 'Test Name', 'test.pdf', 'Notes');

CREATE TABLE single_form_route_missing_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);
