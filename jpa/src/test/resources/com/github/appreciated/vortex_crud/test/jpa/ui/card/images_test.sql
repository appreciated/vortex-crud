-- Seed data for jpa projects tests
CREATE TABLE card_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

INSERT INTO card_images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
