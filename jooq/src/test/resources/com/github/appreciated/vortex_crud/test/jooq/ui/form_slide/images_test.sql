-- Seed data for jOOQ projects tests
DELETE
FROM images;

INSERT INTO images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
