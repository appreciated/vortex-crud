-- ============================================================================
-- Project Management Platform - Simplified Schema
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
    progress_percentage INTEGER DEFAULT 0,
    color VARCHAR(20),
    is_archived INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT
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
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Tasks / Issues
-- ============================================================================
CREATE TABLE task (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
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
    due_date TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
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

-- Comments
-- ============================================================================
CREATE TABLE task_comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Indexes for Performance
-- ============================================================================
CREATE INDEX idx_task_project ON task(project_id);
CREATE INDEX idx_task_assignee ON task(assignee_id);
CREATE INDEX idx_task_status ON task(status);
CREATE INDEX idx_task_parent ON task(parent_task_id);
CREATE INDEX idx_comment_task ON task_comment(task_id);
CREATE INDEX idx_custom_field_entity ON custom_field_definition(entity_type);

-- Minimal Sample Data
-- ============================================================================

-- Sample Labels
INSERT INTO label (name, color, description) VALUES
('bug', '#d73a4a', 'Something is not working'),
('enhancement', '#a2eeef', 'New feature or request');

-- Sample Project
INSERT INTO project (name, code, description, status, priority, start_date, end_date, owner_id) VALUES
('Demo Project', 'DEMO', 'Sample project for testing', 'active', 'high', '2025-01-01', '2025-12-31', 1);

-- Sample Milestone
INSERT INTO milestone (project_id, title, description, due_date, status) VALUES
(1, 'v1.0 Release', 'First release', '2025-06-01', 'open');

-- Sample Tasks
INSERT INTO task (project_id, task_number, title, description, task_type, status, priority, reporter_id) VALUES
(1, 1, 'Setup project', 'Initialize project structure', 'task', 'done', 'high', 1),
(1, 2, 'Implement feature X', 'Add new feature', 'story', 'in_progress', 'medium', 1);

-- Sample Custom Field Definitions
INSERT INTO custom_field_definition (entity_type, field_name, field_label, field_type, is_required, description) VALUES
('project', 'client_name', 'Client Name', 'text', 1, 'Name of the client'),
('task', 'severity', 'Bug Severity', 'select', 0, 'Severity level for bugs');
