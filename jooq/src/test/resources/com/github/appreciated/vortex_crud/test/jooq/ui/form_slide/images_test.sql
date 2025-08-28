-- Seed data for jOOQ projects tests
DELETE
FROM from_slide_images;

INSERT INTO from_slide_images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png');
