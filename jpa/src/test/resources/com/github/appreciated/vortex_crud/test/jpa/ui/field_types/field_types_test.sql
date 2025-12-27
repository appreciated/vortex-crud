DROP TABLE IF EXISTS field_types_full_test_tags;
DROP TABLE IF EXISTS field_types_full_test;

CREATE TABLE field_types_full_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT,
    date_range       VARCHAR(500),
    date_time_range  VARCHAR(500)
);

CREATE TABLE field_types_full_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

INSERT INTO field_types_full_test (id, name, pdf_doc, notes) VALUES (1, 'Test Entity 1', 'test.pdf', '## Header');
INSERT INTO field_types_full_test_tags (entity_id, tag_value) VALUES (1, 'tag1');
INSERT INTO field_types_full_test_tags (entity_id, tag_value) VALUES (1, 'tag2');
