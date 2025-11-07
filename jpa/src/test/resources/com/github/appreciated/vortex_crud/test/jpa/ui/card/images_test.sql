-- Seed data for jpa projects tests
DROP TABLE IF EXISTS card_images;

CREATE TABLE card_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

INSERT INTO card_images (id, title, url)
VALUES (1, 'Red', './red.png'),
       (2, 'Green', './green.png');
