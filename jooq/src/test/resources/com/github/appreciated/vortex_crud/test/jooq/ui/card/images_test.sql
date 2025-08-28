-- Seed data for jOOQ projects tests
DELETE
FROM card_images;

INSERT INTO card_images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
