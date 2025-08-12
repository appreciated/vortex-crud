-- liquibase formatted sql

-- changeset Create test_table:1
CREATE TABLE test_table
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL,
    age  INTEGER      NOT NULL
);

-- changeset Create validation_test (Field Validation tables):2
CREATE TABLE validation_test
(
    id             INTEGER      NOT NULL,
    required_field VARCHAR(255) NOT NULL,
    email_field    VARCHAR(255),
    numeric_field  decimal(3,2) CHECK (numeric_field > 0),
    date_field     DATE,
    enum_field     VARCHAR(20),
    PRIMARY KEY (id)
);

-- changeset Create many_to_many_item:3
CREATE TABLE many_to_many_item
(
    id   INTEGER      NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- changeset Create many_to_many_item_relation with composite PK and FKs:4
CREATE TABLE many_to_many_item_relation
(
    item_id         INTEGER NOT NULL,
    related_item_id INTEGER NOT NULL,
    CONSTRAINT pk_many_to_many_item_relation PRIMARY KEY (item_id, related_item_id),
    CONSTRAINT fk_mtm_item FOREIGN KEY (item_id) REFERENCES many_to_many_item (id),
    CONSTRAINT fk_mtm_related_item FOREIGN KEY (related_item_id) REFERENCES many_to_many_item (id)
);

-- changeset Create one_to_many_parent:5
CREATE TABLE one_to_many_parent
(
    id   INTEGER      NOT NULL,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- changeset Create one_to_many_child with nullable FK to parent:6
CREATE TABLE one_to_many_child
(
    id        INTEGER      NOT NULL,
    name      VARCHAR(255) NOT NULL,
    parent_id INTEGER,
    PRIMARY KEY (id),
    CONSTRAINT fk_otm_parent FOREIGN KEY (parent_id) REFERENCES one_to_many_parent (id)
);