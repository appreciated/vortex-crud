DROP TABLE IF EXISTS datastore_dropdown_test;
CREATE TABLE datastore_dropdown_test (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

INSERT INTO datastore_dropdown_test (id, name) VALUES (1, 'Project A');
INSERT INTO datastore_dropdown_test (id, name) VALUES (2, 'Project B');
