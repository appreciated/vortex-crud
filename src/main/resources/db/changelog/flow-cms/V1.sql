-- Table for users
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    role          VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for roles (optional, if roles are to be managed dynamically)
CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
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
