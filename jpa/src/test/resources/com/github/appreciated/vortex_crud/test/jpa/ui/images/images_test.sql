-- Seed data for JPA images tests
DELETE
FROM images;

INSERT INTO images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
