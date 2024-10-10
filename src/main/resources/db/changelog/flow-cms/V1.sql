-- Table for users
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for roles
CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Join table to manage the many-to-many relationship between users and roles
CREATE TABLE user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Table for audit logs
CREATE TABLE audit_log
(
    id                SERIAL PRIMARY KEY,
    user_id           INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE, -- The user who performed the action
    action            VARCHAR(50) NOT NULL,                                         -- Type of action, e.g., "create", "update", "delete", "login", "logout"
    target_collection VARCHAR(255),                                                 -- The collection the action was applied to
    target_record_id  INT,                                                          -- ID of the record the action was applied to
    description       TEXT,                                                         -- Description of the action
    timestamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           -- Timestamp of the action
);
