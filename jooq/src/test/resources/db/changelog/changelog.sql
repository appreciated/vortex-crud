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
    enum_field     VARCHAR(20),
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
-- changeset Create users table for project tasks:7
CREATE TABLE users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create projects table for project and i18n tests:8
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

-- changeset Create tasks table for completeness:9
CREATE TABLE tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES users (id)
);

-- changeset Create task_comments table:10
CREATE TABLE task_comments
(
    id           INTEGER PRIMARY KEY,
    comment_text VARCHAR(1000),
    user_id      INT,
    created_at   TIMESTAMP,
    task_id      INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (task_id) REFERENCES tasks (id)
);

-- changeset Create task_has_task relation table:11
CREATE TABLE task_has_task
(
    id              INTEGER PRIMARY KEY,
    task_id         INT NOT NULL,
    related_task_id INT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (task_id, related_task_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id),
    FOREIGN KEY (related_task_id) REFERENCES tasks (id)
);

-- changeset Create images table for image tests:12
CREATE TABLE images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);
