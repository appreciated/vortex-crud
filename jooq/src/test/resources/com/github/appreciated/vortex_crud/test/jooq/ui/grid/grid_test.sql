-- Seed data for jOOQ image tests
DELETE
FROM grid_images;

INSERT INTO grid_images (id, title, url)
VALUES (1, 'ItemOne', './red.png'),
       (2, 'ItemTwo', './green.png');
