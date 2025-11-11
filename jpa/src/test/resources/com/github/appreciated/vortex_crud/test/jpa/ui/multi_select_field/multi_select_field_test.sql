-- Seed data for jpa multi select field tests
DROP TABLE IF EXISTS product_category;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product_category
(
    product_id  INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

-- Insert test categories
INSERT INTO category (id, name)
VALUES (1, 'Electronics'),
       (2, 'Books'),
       (3, 'Clothing'),
       (4, 'Home & Garden'),
       (5, 'Sports');

-- Insert test product
INSERT INTO product (id, name)
VALUES (1, 'Test Item');

-- Associate product with some categories
INSERT INTO product_category (product_id, category_id)
VALUES (1, 1),
       (1, 2);
