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
    numeric_field  DOUBLE PRECISION CHECK (numeric_field > 0),
    date_field     DATE,
    datetime_field TIMESTAMP,
    enum_field     VARCHAR(20),
    checkbox_field BOOLEAN,
    image_field    VARCHAR(255),
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

-- changeset Create users table for project tasks:8
CREATE TABLE subroute_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create users table for project tasks:9
CREATE TABLE master_detail_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create users table for project tasks:10
CREATE TABLE kanban_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create projects table for project and i18n tests:11
CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

-- changeset Create tasks table for completeness:12
CREATE TABLE subroute_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    row_index   INTEGER,
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES subroute_users (id)
);

-- changeset Create tasks table for completeness:13
CREATE TABLE master_detail_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    row_index   INTEGER,
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES master_detail_users (id)
);

-- changeset Create tasks table for completeness:14
CREATE TABLE kanban_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    row_index   INTEGER,
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES kanban_users (id)
);

-- changeset Create images table for image tests:15
CREATE TABLE card_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

-- changeset Create images table for image tests:16
CREATE TABLE grid_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);


-- changeset Create images table for image tests:17
CREATE TABLE i18n_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

