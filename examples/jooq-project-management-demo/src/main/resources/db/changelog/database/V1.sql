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

-- Sample Data
-- ============================================================================

-- Sample Teams
INSERT INTO team (name, description) VALUES
('Engineering', 'Software development team'),
('Product', 'Product management team'),
('Design', 'UI/UX design team');

-- Sample Labels
INSERT INTO label (name, color, description, category) VALUES
('bug', '#d73a4a', 'Something is not working', 'type'),
('enhancement', '#a2eeef', 'New feature or request', 'type'),
('documentation', '#0075ca', 'Improvements or additions to documentation', 'type'),
('high-priority', '#b60205', 'High priority item', 'priority'),
('good-first-issue', '#7057ff', 'Good for newcomers', 'difficulty'),
('help-wanted', '#008672', 'Extra attention is needed', 'status');

-- Sample Projects
INSERT INTO project (name, code, description, status, priority, start_date, end_date, owner_id, team_id, color) VALUES
('Vortex CRUD Framework', 'VCRUD', 'Development of the Vortex CRUD framework with advanced features', 'active', 'high', '2025-01-01', '2025-12-31', 1, 1, '#3b82f6'),
('Mobile App Redesign', 'MOBILE', 'Complete redesign of the mobile application', 'planning', 'medium', '2025-03-01', '2025-06-30', 1, 3, '#10b981'),
('Customer Portal', 'PORTAL', 'New customer self-service portal', 'active', 'high', '2025-02-01', '2025-08-31', 1, 1, '#f59e0b');

-- Sample Sprints
INSERT INTO sprint (project_id, name, goal, start_date, end_date, status, sprint_number, capacity_hours) VALUES
(1, 'Sprint 1 - Foundation', 'Set up project structure and core entities', '2025-01-01', '2025-01-14', 'completed', 1, 80),
(1, 'Sprint 2 - Custom Fields', 'Implement custom fields functionality', '2025-01-15', '2025-01-28', 'active', 2, 80),
(1, 'Sprint 3 - UI Polish', 'Polish user interface and add advanced views', '2025-01-29', '2025-02-11', 'planned', 3, 80);

-- Sample Milestones
INSERT INTO milestone (project_id, title, description, due_date, status, completion_percentage) VALUES
(1, 'Alpha Release', 'First functional version with basic features', '2025-02-15', 'open', 75),
(1, 'Beta Release', 'Feature-complete version for testing', '2025-04-15', 'open', 30),
(2, 'Design Complete', 'All UI/UX designs finalized', '2025-04-01', 'open', 0),
(3, 'MVP Launch', 'Minimum viable product ready for customers', '2025-05-01', 'open', 45);

-- Sample Tasks
INSERT INTO task (project_id, sprint_id, milestone_id, task_number, title, description, task_type, status, priority, assignee_id, reporter_id, estimated_hours, story_points, due_date) VALUES
(1, 2, 1, 1, 'Design custom fields database schema', 'Create comprehensive database schema for custom fields with meta table', 'task', 'done', 'high', 1, 1, 4, 3, '2025-01-16'),
(1, 2, 1, 2, 'Implement custom field service layer', 'Build service layer for managing custom field definitions', 'task', 'in_progress', 'high', 1, 1, 8, 5, '2025-01-20'),
(1, 2, 1, 3, 'Create UI for custom field management', 'Admin interface for creating and managing custom fields', 'story', 'todo', 'medium', 1, 1, 12, 8, '2025-01-25'),
(1, 2, 1, 4, 'Add custom fields to entity forms', 'Dynamically render custom fields in entity forms', 'task', 'todo', 'high', 1, 1, 8, 5, '2025-01-27'),
(1, NULL, 1, 5, 'Fix task filtering bug', 'Task filters not working correctly on kanban board', 'bug', 'in_review', 'high', 1, 1, 2, 2, '2025-01-18'),
(2, NULL, 3, 1, 'Create wireframes for new mobile design', 'Sketch out all major screens', 'task', 'done', 'high', 1, 1, 16, 8, '2025-03-10'),
(2, NULL, 3, 2, 'User research for mobile app', 'Conduct interviews with 10 users', 'task', 'in_progress', 'medium', 1, 1, 20, 5, '2025-03-20'),
(3, NULL, 4, 1, 'Set up customer authentication', 'Implement OAuth2 authentication for customers', 'story', 'done', 'highest', 1, 1, 16, 13, '2025-02-15'),
(3, NULL, 4, 2, 'Build customer dashboard', 'Overview dashboard showing key metrics', 'story', 'in_progress', 'high', 1, 1, 24, 8, '2025-02-28'),
(3, NULL, 4, 3, 'Implement ticket submission form', 'Allow customers to submit support tickets', 'task', 'todo', 'medium', 1, 1, 8, 5, '2025-03-05');

-- Sample Task Comments
INSERT INTO task_comment (task_id, author_id, content) VALUES
(1, 1, 'Completed the initial schema design. Added support for JSON fields and meta table.'),
(1, 1, 'Schema reviewed and approved. Moving to implementation.'),
(2, 1, 'Started working on the CRUD operations for custom field definitions.'),
(5, 1, 'Found the issue - filter was not being applied correctly. Fix in progress.');

-- Sample Time Entries
INSERT INTO time_entry (task_id, user_id, hours, date, description, is_billable) VALUES
(1, 1, 4.0, '2025-01-16', 'Database schema design and documentation', 1),
(1, 1, 2.0, '2025-01-16', 'Schema review and revisions', 1),
(2, 1, 6.5, '2025-01-17', 'Implementation of custom field service', 1),
(5, 1, 1.5, '2025-01-18', 'Bug investigation and fix', 1);

-- Sample Task Labels
INSERT INTO task_label (task_id, label_id) VALUES
(5, 1), -- Bug
(3, 2), -- Enhancement
(4, 2), -- Enhancement
(3, 5), -- Good first issue
(2, 4); -- High priority

-- Sample Custom Field Definitions
INSERT INTO custom_field_definition (entity_type, field_name, field_label, field_type, field_order, is_required, options, description) VALUES
('project', 'client_name', 'Client Name', 'text', 1, 1, NULL, 'Name of the client for this project'),
('project', 'contract_value', 'Contract Value', 'number', 2, 0, NULL, 'Total contract value in USD'),
('project', 'project_type', 'Project Type', 'select', 3, 0, '["Internal", "Client", "Open Source", "R&D"]', 'Type of project'),
('task', 'severity', 'Bug Severity', 'select', 1, 0, '["Minor", "Major", "Critical", "Blocker"]', 'Severity level for bugs'),
('task', 'test_coverage', 'Test Coverage %', 'number', 2, 0, NULL, 'Percentage of code covered by tests'),
('task', 'requires_documentation', 'Requires Documentation', 'checkbox', 3, 0, NULL, 'Whether this task requires documentation updates');
