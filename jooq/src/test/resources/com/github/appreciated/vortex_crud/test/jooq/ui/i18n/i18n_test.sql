-- Seed data for jOOQ i18n smoke tests
DELETE
FROM i18n_images;

INSERT INTO i18n_images (id, title, url)
VALUES (1, 'Red', './red.png'),
       (2, 'Green', './green.png');
