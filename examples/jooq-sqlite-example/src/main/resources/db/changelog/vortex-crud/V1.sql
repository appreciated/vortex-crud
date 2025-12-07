-- liquibase formatted sql

-- changeset jooq-sqlite-example-vortex-crud:1
CREATE TABLE users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset jooq-sqlite-example-vortex-crud:2
CREATE TABLE roles
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- changeset jooq-sqlite-example-vortex-crud:3
CREATE TABLE user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- changeset jooq-sqlite-example-vortex-crud:4
CREATE TABLE audit_log
(
    id                INTEGER PRIMARY KEY,
    user_id           INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE, -- The user who performed the action
    action            VARCHAR(50) NOT NULL,                                         -- Type of action, e.g., "create", "update", "delete", "login", "logout"
    target_collection VARCHAR(255),                                                 -- The collection the action was applied to
    target_record_id  INT,                                                          -- ID of the record the action was applied to
    description       TEXT,                                                         -- Description of the action
    timestamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           -- Timestamp of the action
);
