-- ============================================================================
-- Project Management Platform - Database Schema
-- ============================================================================

-- Custom Fields Meta System
-- ============================================================================
CREATE TABLE custom_field_definition (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    field_label VARCHAR(100) NOT NULL,
    field_type VARCHAR(50) NOT NULL,
    field_order INTEGER DEFAULT 0,
    is_required INTEGER DEFAULT 0,
    default_value TEXT,
    options TEXT,
    validation_rules TEXT,
    description TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active INTEGER DEFAULT 1,
    UNIQUE(entity_type, field_name)
);

-- Teams & Organization
-- ============================================================================
CREATE TABLE team (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    avatar_url VARCHAR(255),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active INTEGER DEFAULT 1,
    custom_fields TEXT
);

CREATE TABLE team_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    team_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',
    joined_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
    UNIQUE(team_id, user_id)
);

-- Projects
-- ============================================================================
CREATE TABLE project (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    status VARCHAR(50) DEFAULT 'active',
    priority VARCHAR(50) DEFAULT 'medium',
    start_date TEXT,
    end_date TEXT,
    owner_id INTEGER,
    team_id INTEGER,
    progress_percentage INTEGER DEFAULT 0,
    budget REAL,
    actual_cost REAL,
    color VARCHAR(20),
    is_archived INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE SET NULL
);

-- Sprints / Iterations
-- ============================================================================
CREATE TABLE sprint (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    goal TEXT,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'planned',
    sprint_number INTEGER,
    capacity_hours INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    UNIQUE(project_id, sprint_number)
);

-- Milestones
-- ============================================================================
CREATE TABLE milestone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date TEXT,
    status VARCHAR(50) DEFAULT 'open',
    completion_percentage INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    completed_at TEXT,
    custom_fields TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- Labels / Tags
-- ============================================================================
CREATE TABLE label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Tasks / Issues
-- ============================================================================
CREATE TABLE task (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    sprint_id INTEGER,
    milestone_id INTEGER,
    parent_task_id INTEGER,
    task_number INTEGER NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    task_type VARCHAR(50) DEFAULT 'task',
    status VARCHAR(50) DEFAULT 'todo',
    priority VARCHAR(50) DEFAULT 'medium',
    assignee_id INTEGER,
    reporter_id INTEGER NOT NULL,
    estimated_hours REAL,
    actual_hours REAL DEFAULT 0,
    remaining_hours REAL,
    story_points INTEGER,
    due_date TEXT,
    start_date TEXT,
    resolution VARCHAR(50),
    resolution_date TEXT,
    kanban_column VARCHAR(50),
    kanban_order INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    FOREIGN KEY (sprint_id) REFERENCES sprint(id) ON DELETE SET NULL,
    FOREIGN KEY (milestone_id) REFERENCES milestone(id) ON DELETE SET NULL,
    FOREIGN KEY (parent_task_id) REFERENCES task(id) ON DELETE CASCADE,
    UNIQUE(project_id, task_number)
);

-- Task Labels (Many-to-Many)
-- ============================================================================
CREATE TABLE task_label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    label_id INTEGER NOT NULL,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (label_id) REFERENCES label(id) ON DELETE CASCADE,
    UNIQUE(task_id, label_id)
);

-- Task Watchers
-- ============================================================================
CREATE TABLE task_watcher (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    UNIQUE(task_id, user_id)
);

-- Comments / Activity
-- ============================================================================
CREATE TABLE task_comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    is_internal INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Time Tracking
-- ============================================================================
CREATE TABLE time_entry (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    hours REAL NOT NULL,
    date TEXT NOT NULL,
    description TEXT,
    is_billable INTEGER DEFAULT 1,
    billing_rate REAL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Task Dependencies
-- ============================================================================
CREATE TABLE task_dependency (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    depends_on_task_id INTEGER NOT NULL,
    dependency_type VARCHAR(50) DEFAULT 'blocks',
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (depends_on_task_id) REFERENCES task(id) ON DELETE CASCADE,
    UNIQUE(task_id, depends_on_task_id)
);

-- Attachments
-- ============================================================================
CREATE TABLE attachment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INTEGER NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size INTEGER,
    mime_type VARCHAR(100),
    uploaded_by INTEGER NOT NULL,
    uploaded_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Project Members
-- ============================================================================
CREATE TABLE project_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',
    joined_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    UNIQUE(project_id, user_id)
);

-- Activity Log
-- ============================================================================
CREATE TABLE activity_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    description TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Notifications
-- ============================================================================
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INTEGER NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read INTEGER DEFAULT 0,
    read_at TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for Performance
-- ============================================================================
CREATE INDEX idx_task_project ON task(project_id);
CREATE INDEX idx_task_sprint ON task(sprint_id);
CREATE INDEX idx_task_assignee ON task(assignee_id);
CREATE INDEX idx_task_status ON task(status);
CREATE INDEX idx_task_parent ON task(parent_task_id);
CREATE INDEX idx_time_entry_task ON time_entry(task_id);
CREATE INDEX idx_time_entry_user ON time_entry(user_id);
CREATE INDEX idx_time_entry_date ON time_entry(date);
CREATE INDEX idx_comment_task ON task_comment(task_id);
CREATE INDEX idx_activity_entity ON activity_log(entity_type, entity_id);
CREATE INDEX idx_notification_user ON notification(user_id, is_read);
CREATE INDEX idx_custom_field_entity ON custom_field_definition(entity_type);

-- Minimal Sample Data
-- ============================================================================

-- Sample Team
INSERT INTO team (name, description) VALUES
('Engineering', 'Software development team');

-- Sample Labels
INSERT INTO label (name, color, description) VALUES
('bug', '#d73a4a', 'Something is not working'),
('enhancement', '#a2eeef', 'New feature or request');

-- Sample Project
INSERT INTO project (name, code, description, status, priority, start_date, end_date, owner_id, team_id) VALUES
('Demo Project', 'DEMO', 'Sample project for testing', 'active', 'high', '2025-01-01', '2025-12-31', 1, 1);

-- Sample Sprint
INSERT INTO sprint (project_id, name, goal, start_date, end_date, status, sprint_number, capacity_hours) VALUES
(1, 'Sprint 1', 'Initial sprint', '2025-01-01', '2025-01-14', 'active', 1, 80);

-- Sample Milestone
INSERT INTO milestone (project_id, title, description, due_date, status) VALUES
(1, 'v1.0 Release', 'First release', '2025-06-01', 'open');

-- Sample Tasks
INSERT INTO task (project_id, sprint_id, task_number, title, description, task_type, status, priority, reporter_id) VALUES
(1, 1, 1, 'Setup project', 'Initialize project structure', 'task', 'done', 'high', 1),
(1, 1, 2, 'Implement feature X', 'Add new feature', 'story', 'in_progress', 'medium', 1);

-- Sample Custom Field Definition
INSERT INTO custom_field_definition (entity_type, field_name, field_label, field_type, is_required, description) VALUES
('project', 'client_name', 'Client Name', 'text', 1, 'Name of the client'),
('task', 'severity', 'Bug Severity', 'select', 0, 'Severity level for bugs');
