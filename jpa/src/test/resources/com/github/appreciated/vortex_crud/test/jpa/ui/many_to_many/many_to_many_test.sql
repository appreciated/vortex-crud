DROP TABLE IF EXISTS many_to_many_item_relation;
DROP TABLE IF EXISTS many_to_many_item;

CREATE TABLE many_to_many_item
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE many_to_many_item_relation
(
    item_id         INTEGER NOT NULL,
    related_item_id INTEGER NOT NULL,
    PRIMARY KEY (item_id, related_item_id),
    FOREIGN KEY (item_id) REFERENCES many_to_many_item (id),
    FOREIGN KEY (related_item_id) REFERENCES many_to_many_item (id)
);

-- Seed data for jOOQ many-to-many tests
INSERT INTO many_to_many_item (id, name)
VALUES (1, 'Item 1'),
       (2, 'Item 2'),
       (3, 'Item 3');

-- Relations: 1 related to 2 and 3
INSERT INTO many_to_many_item_relation (item_id, related_item_id)
VALUES (1, 2),
       (1, 3);

