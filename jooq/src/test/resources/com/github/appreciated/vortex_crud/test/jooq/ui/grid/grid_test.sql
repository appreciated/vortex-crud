-- Seed data for jOOQ image tests
DELETE
FROM grid_images;

INSERT INTO grid_images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
