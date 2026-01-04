-- liquibase formatted sql

-- changeset project-management-demo:13
-- Notifications
-- ============================================================================
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notification_user ON notification(user_id, is_read);
