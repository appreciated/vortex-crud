-- Seed data for jpa image tests
DROP TABLE IF EXISTS grid_images;

CREATE TABLE grid_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

INSERT INTO grid_images (id, title, url)
VALUES (1, 'Red', './red.png'),
       (2, 'Green', './green.png');
