-- liquibase formatted sql

-- changeset dev-platform-demo:15
CREATE TABLE ssh_keys (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    public_key TEXT NOT NULL,
    name VARCHAR(100),
    fingerprint VARCHAR(100),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_ssh_keys_user ON ssh_keys(user_id);
