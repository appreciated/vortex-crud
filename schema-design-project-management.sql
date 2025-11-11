-- ============================================================================
-- VORTEX-CRUD: Project Management Platform Demo
-- Database Schema Design (SQLite with jOOQ)
-- ============================================================================

-- Custom Fields Meta System
-- ============================================================================
CREATE TABLE custom_field_definition (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,          -- 'project', 'task', 'sprint', etc.
    field_name VARCHAR(100) NOT NULL,
    field_label VARCHAR(100) NOT NULL,
    field_type VARCHAR(50) NOT NULL,           -- 'text', 'number', 'date', 'select', 'multiselect', 'checkbox'
    field_order INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT FALSE,
    default_value TEXT,
    options TEXT,                               -- JSON array for select/multiselect: ["Option 1", "Option 2"]
    validation_rules TEXT,                      -- JSON: {"min": 0, "max": 100, "pattern": "regex"}
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(entity_type, field_name)
);

-- Teams & Organization
-- ============================================================================
CREATE TABLE team (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    custom_fields TEXT                          -- JSON for custom field values
);

CREATE TABLE team_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    team_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',          -- 'owner', 'admin', 'member'
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
    UNIQUE(team_id, user_id)
);

-- Projects
-- ============================================================================
CREATE TABLE project (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(200) NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,           -- Short code: 'PROJ', 'DEV', etc.
    description TEXT,
    status VARCHAR(50) DEFAULT 'active',        -- 'planning', 'active', 'on_hold', 'completed', 'cancelled'
    priority VARCHAR(50) DEFAULT 'medium',      -- 'low', 'medium', 'high', 'critical'
    start_date DATE,
    end_date DATE,
    owner_id INTEGER,
    team_id INTEGER,
    progress_percentage INTEGER DEFAULT 0,
    budget DECIMAL(15, 2),
    actual_cost DECIMAL(15, 2),
    color VARCHAR(20),                          -- Hex color for UI
    is_archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,                         -- JSON for custom field values
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE SET NULL
);

-- Sprints / Iterations
-- ============================================================================
CREATE TABLE sprint (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    goal TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'planned',       -- 'planned', 'active', 'completed', 'cancelled'
    sprint_number INTEGER,
    capacity_hours INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    due_date DATE,
    status VARCHAR(50) DEFAULT 'open',          -- 'open', 'completed', 'overdue'
    completion_percentage INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- Labels / Tags
-- ============================================================================
CREATE TABLE label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL,                 -- Hex color
    description TEXT,
    category VARCHAR(50),                       -- 'priority', 'type', 'component', etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tasks / Issues
-- ============================================================================
CREATE TABLE task (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    sprint_id INTEGER,
    milestone_id INTEGER,
    parent_task_id INTEGER,                     -- For sub-tasks
    task_number INTEGER NOT NULL,               -- Auto-increment per project: PROJ-1, PROJ-2
    title VARCHAR(300) NOT NULL,
    description TEXT,
    task_type VARCHAR(50) DEFAULT 'task',       -- 'task', 'bug', 'story', 'epic', 'subtask'
    status VARCHAR(50) DEFAULT 'todo',          -- 'todo', 'in_progress', 'in_review', 'done', 'blocked'
    priority VARCHAR(50) DEFAULT 'medium',      -- 'lowest', 'low', 'medium', 'high', 'highest'
    assignee_id INTEGER,
    reporter_id INTEGER NOT NULL,
    estimated_hours DECIMAL(8, 2),
    actual_hours DECIMAL(8, 2) DEFAULT 0,
    remaining_hours DECIMAL(8, 2),
    story_points INTEGER,
    due_date DATE,
    start_date DATE,
    resolution VARCHAR(50),                     -- 'fixed', 'wont_fix', 'duplicate', etc.
    resolution_date TIMESTAMP,
    kanban_column VARCHAR(50),                  -- For kanban view
    kanban_order INTEGER,                       -- Order within column
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

-- Task Watchers (Users watching a task for updates)
-- ============================================================================
CREATE TABLE task_watcher (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    is_internal BOOLEAN DEFAULT FALSE,          -- Internal notes vs public comments
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Time Tracking
-- ============================================================================
CREATE TABLE time_entry (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    hours DECIMAL(8, 2) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    is_billable BOOLEAN DEFAULT TRUE,
    billing_rate DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

-- Task Dependencies
-- ============================================================================
CREATE TABLE task_dependency (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    task_id INTEGER NOT NULL,                   -- The dependent task
    depends_on_task_id INTEGER NOT NULL,        -- The task it depends on
    dependency_type VARCHAR(50) DEFAULT 'blocks', -- 'blocks', 'is_blocked_by', 'relates_to'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE,
    FOREIGN KEY (depends_on_task_id) REFERENCES task(id) ON DELETE CASCADE,
    UNIQUE(task_id, depends_on_task_id)
);

-- Attachments
-- ============================================================================
CREATE TABLE attachment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,           -- 'task', 'project', 'comment'
    entity_id INTEGER NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    uploaded_by INTEGER NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Project Members (Many-to-Many)
-- ============================================================================
CREATE TABLE project_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',          -- 'owner', 'admin', 'developer', 'viewer'
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
    UNIQUE(project_id, user_id)
);

-- Activity Log (for timeline/history)
-- ============================================================================
CREATE TABLE activity_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,           -- 'task', 'project', 'sprint'
    entity_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    action VARCHAR(50) NOT NULL,                -- 'created', 'updated', 'commented', 'status_changed'
    old_value TEXT,                             -- JSON
    new_value TEXT,                             -- JSON
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Notifications
-- ============================================================================
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id INTEGER NOT NULL,
    notification_type VARCHAR(50) NOT NULL,     -- 'task_assigned', 'comment_added', 'status_changed'
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
