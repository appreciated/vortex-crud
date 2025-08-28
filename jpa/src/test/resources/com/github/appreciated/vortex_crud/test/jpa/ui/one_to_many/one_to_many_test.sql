-- Seed data for jOOQ one-to-many tests
DROP TABLE IF EXISTS one_to_many_child;
DROP TABLE IF EXISTS one_to_many_parent;

CREATE TABLE one_to_many_parent
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE one_to_many_child
(
    id        INTEGER PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    parent_id INTEGER,
    CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES one_to_many_parent (id)
);

INSERT INTO one_to_many_parent (id, name)
VALUES (1, 'Parent A'),
       (2, 'Parent B');
INSERT INTO one_to_many_child (id, name, parent_id)
VALUES (1, 'Child A1', 1),
       (2, 'Child A2', 1),
       (3, 'Child B1', 2);
