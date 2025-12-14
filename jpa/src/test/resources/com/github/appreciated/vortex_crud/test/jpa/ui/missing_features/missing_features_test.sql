-- Drop tables in correct order (child tables first due to foreign key constraints)
DROP TABLE IF EXISTS missing_features_test_relations;
DROP TABLE IF EXISTS missing_features_test_tags;
DROP TABLE IF EXISTS missing_features_test;
DROP TABLE IF EXISTS missing_features_referenced;

-- Create referenced entity table
CREATE TABLE missing_features_referenced
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL
);

-- Create main test entity table
CREATE TABLE missing_features_test
(
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    name              VARCHAR(255) NOT NULL,
    pdf_doc           VARCHAR(255),
    notes             TEXT,
    referenced_id     INTEGER,
    markdown_content  TEXT,
    file_attachment   VARCHAR(255),
    price             DECIMAL(10, 2),
    FOREIGN KEY (referenced_id) REFERENCES missing_features_referenced (id)
);

-- Create ElementCollection table for tags
CREATE TABLE missing_features_test_tags
(
    entity_id INTEGER      NOT NULL,
    tag_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (entity_id, tag_value),
    FOREIGN KEY (entity_id) REFERENCES missing_features_test (id)
);

-- Create ManyToMany join table
CREATE TABLE missing_features_test_relations
(
    test_id       INTEGER NOT NULL,
    referenced_id INTEGER NOT NULL,
    PRIMARY KEY (test_id, referenced_id),
    FOREIGN KEY (test_id) REFERENCES missing_features_test (id),
    FOREIGN KEY (referenced_id) REFERENCES missing_features_referenced (id)
);

-- Insert test data
INSERT INTO missing_features_referenced (id, name) VALUES (1, 'Ref 1');
INSERT INTO missing_features_referenced (id, name) VALUES (2, 'Ref 2');

INSERT INTO missing_features_test (id, name, pdf_doc, notes, referenced_id, markdown_content, file_attachment, price)
VALUES (1, 'Test Entity 1', 'test.pdf', '## Header', 1, '# Markdown', 'file.txt', 99.99);

INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag1');
INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag2');

INSERT INTO missing_features_test_relations (test_id, referenced_id) VALUES (1, 1);
INSERT INTO missing_features_test_relations (test_id, referenced_id) VALUES (1, 2);
