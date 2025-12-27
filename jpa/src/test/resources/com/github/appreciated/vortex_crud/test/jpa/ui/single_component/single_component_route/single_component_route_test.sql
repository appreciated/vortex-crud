DROP TABLE IF EXISTS single_component_route_test_tags;
DROP TABLE IF EXISTS single_component_route_test;

CREATE TABLE single_component_route_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

CREATE TABLE single_component_route_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

INSERT INTO single_component_route_test (id, name, pdf_doc, notes) VALUES (1, 'Test Entity 1', 'test.pdf', '## Header');
INSERT INTO single_component_route_test_tags (entity_id, tag_value) VALUES (1, 'tag1');
INSERT INTO single_component_route_test_tags (entity_id, tag_value) VALUES (1, 'tag2');
