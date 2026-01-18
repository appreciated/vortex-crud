-- liquibase formatted sql

-- changeset project-management-demo:13
-- Sprints
-- ============================================================================
CREATE TABLE sprint (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    goal TEXT,
    start_date TEXT,
    end_date TEXT,
    status VARCHAR(50) DEFAULT 'planned',
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

CREATE INDEX idx_sprint_project ON sprint(project_id);

-- changeset project-management-demo:14
-- Add Sprint to Task
-- ============================================================================
ALTER TABLE task ADD COLUMN sprint_id INTEGER REFERENCES sprint(id) ON DELETE SET NULL;
CREATE INDEX idx_task_sprint ON task(sprint_id);
