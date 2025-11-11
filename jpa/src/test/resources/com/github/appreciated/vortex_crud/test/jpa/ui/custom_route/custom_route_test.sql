-- Seed data for CustomRoute tests
DROP TABLE IF EXISTS custom_route_items;

CREATE TABLE custom_route_items
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
);

INSERT INTO custom_route_items (id, name, description) VALUES
  (1, 'Item One', 'First test item'),
  (2, 'Item Two', 'Second test item'),
  (3, 'Item Three', 'Third test item');
