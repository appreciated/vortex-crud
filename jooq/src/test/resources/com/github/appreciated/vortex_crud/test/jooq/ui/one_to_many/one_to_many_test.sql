-- Seed data for jOOQ one-to-many tests
DELETE FROM one_to_many_child;
DELETE FROM one_to_many_parent;

INSERT INTO one_to_many_parent (id, name) VALUES
  (1, 'Parent A'),
  (2, 'Parent B');
INSERT INTO one_to_many_child (id, name, parent_id) VALUES
  (1, 'Child A1', 1),
  (2, 'Child A2', 1),
  (3, 'Child B1', 2);
