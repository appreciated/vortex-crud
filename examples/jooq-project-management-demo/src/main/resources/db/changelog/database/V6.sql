-- liquibase formatted sql

-- changeset project-management-demo:17
-- Dynamic kanban workflow: allowed status transitions live in the database and can be
-- managed at runtime through the workflow route
CREATE TABLE workflow_transition (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,
    from_status VARCHAR(50) NOT NULL,
    to_status VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(entity_type, from_status, to_status)
);

-- Jira-like default flow for tasks
INSERT INTO workflow_transition (entity_type, from_status, to_status, description) VALUES
('task', 'todo', 'in_progress', 'Start working on a task'),
('task', 'todo', 'blocked', 'Task blocked before start'),
('task', 'in_progress', 'in_review', 'Submit for review'),
('task', 'in_progress', 'blocked', 'Work interrupted by a blocker'),
('task', 'in_progress', 'todo', 'Stop progress'),
('task', 'in_review', 'done', 'Review passed'),
('task', 'in_review', 'in_progress', 'Review requires changes'),
('task', 'blocked', 'todo', 'Blocker resolved, back to backlog'),
('task', 'blocked', 'in_progress', 'Blocker resolved, resume work'),
('task', 'done', 'todo', 'Reopen a task');
