-- liquibase formatted sql

-- changeset project-management-demo:11
-- Time Tracking
-- ============================================================================
CREATE TABLE time_entry (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    hours_spent REAL NOT NULL,
    description TEXT,
    entry_date TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_time_entry_task ON time_entry(task_id);
CREATE INDEX idx_time_entry_user ON time_entry(user_id);

-- changeset project-management-demo:12
-- Attachments
-- ============================================================================
CREATE TABLE attachment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    uploader_id INTEGER,
    name VARCHAR(255) NOT NULL,
    media_type VARCHAR(100),
    size INTEGER,
    path VARCHAR(500) NOT NULL,
    uploaded_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (uploader_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_attachment_task ON attachment(task_id);
