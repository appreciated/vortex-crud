-- Seed data for jOOQ i18n smoke tests
DROP TABLE IF EXISTS i18n_images;

CREATE TABLE i18n_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

INSERT INTO i18n_images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
