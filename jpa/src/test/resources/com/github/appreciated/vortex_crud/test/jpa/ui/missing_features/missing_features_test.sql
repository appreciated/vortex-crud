DROP TABLE IF EXISTS missing_features_test_tags;
DROP TABLE IF EXISTS missing_features_test;

CREATE TABLE missing_features_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

CREATE TABLE missing_features_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

INSERT INTO missing_features_test (id, name, pdf_doc, notes) VALUES (1, 'Test Entity 1', 'test.pdf', '## Header');
INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag1');
INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag2');
