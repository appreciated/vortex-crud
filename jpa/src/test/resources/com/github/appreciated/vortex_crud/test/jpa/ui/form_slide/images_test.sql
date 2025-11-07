-- Seed data for JPA image tests
DROP TABLE IF EXISTS images;

CREATE TABLE images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

DELETE
FROM images;

INSERT INTO images (id, title, url)
VALUES (1, 'Red', './red.png'),
       (2, 'Green', './green.png');
