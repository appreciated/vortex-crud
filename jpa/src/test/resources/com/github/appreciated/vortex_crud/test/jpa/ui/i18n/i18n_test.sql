-- Seed data for JPA i18n smoke
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
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
