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

-- changeset Create users table for project tasks:7
CREATE TABLE subroute_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create users table for project tasks:8
CREATE TABLE master_detail_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create users table for project tasks:9
CREATE TABLE kanban_users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

-- changeset Create projects table for project and i18n tests:10
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

-- changeset Create tasks table for completeness:11
CREATE TABLE subroute_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES subroute_users (id)
);

-- changeset Create tasks table for completeness:12
CREATE TABLE master_detail_tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES master_detail_users (id)
);

-- changeset Create tasks table for completeness:13
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

-- changeset Create images table for image tests:14
CREATE TABLE card_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

-- changeset Create images table for image tests:15
CREATE TABLE grid_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);


-- changeset Create images table for image tests:16
CREATE TABLE i18n_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

-- changeset Create images table for image tests:17
CREATE TABLE from_slide_images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR(255),
    url   VARCHAR(255)
);

-- changeset Create additional_fields_test table for testing TextArea, Password, Video, BigDecimal fields:18
CREATE TABLE additional_fields_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

-- changeset Create multi_form_test table for testing MultiFormRoute:19
CREATE TABLE multi_form_test
(
    id           INTEGER PRIMARY KEY,
    profile_name VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    description  TEXT,
    age          INTEGER
);

-- changeset Create field_types_test table for testing various field types:20
CREATE TABLE field_types_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

-- changeset Create field_types_test_tags table for multi-select field:21
CREATE TABLE field_types_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

-- changeset Create global_route_action_test table for testing global route actions:22
CREATE TABLE global_route_action_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

-- changeset Create global_route_action_test_tags table for multi-select field:23
CREATE TABLE global_route_action_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

-- changeset Create single_form_route_test table for testing single form routes:24
CREATE TABLE single_form_route_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    pdf_doc     VARCHAR(255),
    notes       TEXT
);

-- changeset Create single_form_route_test_tags table for multi-select field:25
CREATE TABLE single_form_route_test_tags
(
    entity_id   INTEGER NOT NULL,
    tag_value   VARCHAR(255)
);

-- changeset Create calendar_events table for calendar tests:26
CREATE TABLE calendar_events
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    start_date  TIMESTAMP,
    end_date    TIMESTAMP
);

-- changeset Create missing_features_referenced:27
CREATE TABLE missing_features_referenced
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(255) NOT NULL
);

-- changeset Create missing_features_test:28
CREATE TABLE missing_features_test
(
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    name              VARCHAR(255) NOT NULL,
    pdf_doc           VARCHAR(255),
    notes             TEXT,
    referenced_id     INTEGER,
    markdown_content  TEXT,
    file_attachment   VARCHAR(255),
    price             DECIMAL(10, 2),
    video_url         VARCHAR(255),
    FOREIGN KEY (referenced_id) REFERENCES missing_features_referenced (id)
);

-- changeset Create missing_features_test_tags:29
CREATE TABLE missing_features_test_tags
(
    entity_id INTEGER      NOT NULL,
    tag_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (entity_id, tag_value),
    FOREIGN KEY (entity_id) REFERENCES missing_features_test (id)
);

-- changeset Create missing_features_test_relations:30
CREATE TABLE missing_features_test_relations
(
    test_id       INTEGER NOT NULL,
    referenced_id INTEGER NOT NULL,
    PRIMARY KEY (test_id, referenced_id),
    FOREIGN KEY (test_id) REFERENCES missing_features_test (id),
    FOREIGN KEY (referenced_id) REFERENCES missing_features_referenced (id)
);

-- changeset Create lifecycle_test:31
CREATE TABLE lifecycle_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

-- changeset Create password_test:32
CREATE TABLE password_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

-- changeset Create textarea_test:33
CREATE TABLE textarea_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    password    VARCHAR(255),
    price       DECIMAL(10, 2),
    video_url   VARCHAR(255)
);

-- changeset Create search_route_test:34
CREATE TABLE search_route_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL
);

-- changeset Create notification_panel_test:35
CREATE TABLE notification_panel_test
(
    id          INTEGER PRIMARY KEY,
    message     VARCHAR(255) NOT NULL,
    timestamp   TIMESTAMP,
    read        BOOLEAN
);

-- changeset Create datastore_dropdown_test:36
CREATE TABLE datastore_dropdown_test
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL
);

-- changeset Create checkbox_validation_test:37
CREATE TABLE checkbox_validation_test
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

-- changeset Create date_validation_test:38
CREATE TABLE date_validation_test
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

-- changeset Create datetime_validation_test:39
CREATE TABLE datetime_validation_test
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

-- changeset Create email_validation_test:40
CREATE TABLE email_validation_test
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

-- changeset Create image_validation_test:41
CREATE TABLE image_validation_test
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

-- changeset Create number_validation_test:42
CREATE TABLE number_validation_test
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

-- changeset Create select_validation_test:43
CREATE TABLE select_validation_test
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

-- changeset Create text_validation_test:44
CREATE TABLE text_validation_test
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

-- changeset Create lifecycle_validation_test:45
CREATE TABLE lifecycle_validation_test
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
