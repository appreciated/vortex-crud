-- Seed data for jOOQ many-to-many tests
INSERT INTO many_to_many_item (id, name) VALUES
  (1, 'Item 1'),
  (2, 'Item 2'),
  (3, 'Item 3');

-- Relations: 1 related to 2 and 3
INSERT INTO many_to_many_item_relation (item_id, related_item_id) VALUES
  (1, 2),
  (1, 3);
