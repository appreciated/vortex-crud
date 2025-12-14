INSERT INTO missing_features_referenced (id, name) VALUES (1, 'Ref 1');
INSERT INTO missing_features_referenced (id, name) VALUES (2, 'Ref 2');

INSERT INTO missing_features_test (id, name, pdf_doc, notes, referenced_id, markdown_content, file_attachment, price)
VALUES (1, 'Test Entity 1', 'test.pdf', '## Header', 1, '# Markdown', 'file.txt', 99.99);

INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag1');
INSERT INTO missing_features_test_tags (entity_id, tag_value) VALUES (1, 'tag2');

INSERT INTO missing_features_test_relations (test_id, referenced_id) VALUES (1, 1);
INSERT INTO missing_features_test_relations (test_id, referenced_id) VALUES (1, 2);
