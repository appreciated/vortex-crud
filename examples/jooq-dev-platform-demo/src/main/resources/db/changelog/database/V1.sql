-- ============================================================================
-- Development Platform - Database Schema
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

-- Organizations
-- ============================================================================
CREATE TABLE organization (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(100),
    description TEXT,
    website VARCHAR(255),
    avatar_url VARCHAR(255),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_active INTEGER DEFAULT 1,
    custom_fields TEXT
);

CREATE TABLE organization_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    organization_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',
    joined_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    UNIQUE(organization_id, user_id)
);

-- Repositories
-- ============================================================================
CREATE TABLE repository (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    description TEXT,
    owner_id INTEGER,
    organization_id INTEGER,
    visibility VARCHAR(20) DEFAULT 'private',
    default_branch VARCHAR(100) DEFAULT 'main',
    is_template INTEGER DEFAULT 0,
    is_archived INTEGER DEFAULT 0,
    is_fork INTEGER DEFAULT 0,
    parent_repository_id INTEGER,
    star_count INTEGER DEFAULT 0,
    fork_count INTEGER DEFAULT 0,
    open_issues_count INTEGER DEFAULT 0,
    open_prs_count INTEGER DEFAULT 0,
    size_bytes INTEGER DEFAULT 0,
    language VARCHAR(50),
    topics TEXT,
    homepage_url VARCHAR(255),
    license VARCHAR(50),
    readme_content TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    last_activity_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_repository_id) REFERENCES repository(id) ON DELETE SET NULL
);

-- Repository Members (Collaborators)
-- ============================================================================
CREATE TABLE repository_collaborator (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    permission VARCHAR(50) DEFAULT 'read',
    invited_by INTEGER,
    invited_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, user_id)
);

-- Branches
-- ============================================================================
CREATE TABLE branch (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_default INTEGER DEFAULT 0,
    is_protected INTEGER DEFAULT 0,
    created_from_branch_id INTEGER,
    created_by INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    last_commit_at TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (created_from_branch_id) REFERENCES branch(id) ON DELETE SET NULL,
    UNIQUE(repository_id, name)
);

-- Files / Directories
-- ============================================================================
CREATE TABLE repository_file (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    branch_id INTEGER NOT NULL,
    path VARCHAR(1000) NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent_id INTEGER,
    is_directory INTEGER DEFAULT 0,
    content TEXT,
    size_bytes INTEGER DEFAULT 0,
    mime_type VARCHAR(100),
    language VARCHAR(50),
    encoding VARCHAR(20) DEFAULT 'utf-8',
    sha_hash VARCHAR(64),
    created_by INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_by INTEGER,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branch(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES repository_file(id) ON DELETE CASCADE,
    UNIQUE(repository_id, branch_id, path)
);

-- Labels
-- ============================================================================
CREATE TABLE label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    color VARCHAR(20) NOT NULL,
    description TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, name)
);

-- Milestones
-- ============================================================================
CREATE TABLE milestone (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    state VARCHAR(20) DEFAULT 'open',
    due_date TEXT,
    open_issues_count INTEGER DEFAULT 0,
    closed_issues_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    closed_at TEXT,
    custom_fields TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Issues
-- ============================================================================
CREATE TABLE issue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    issue_number INTEGER NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    state VARCHAR(20) DEFAULT 'open',
    state_reason VARCHAR(50),
    issue_type VARCHAR(50) DEFAULT 'issue',
    priority VARCHAR(50) DEFAULT 'medium',
    author_id INTEGER NOT NULL,
    assignee_id INTEGER,
    milestone_id INTEGER,
    locked INTEGER DEFAULT 0,
    closed_by INTEGER,
    closed_at TEXT,
    comment_count INTEGER DEFAULT 0,
    reaction_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (milestone_id) REFERENCES milestone(id) ON DELETE SET NULL,
    UNIQUE(repository_id, issue_number)
);

-- Issue Labels (Many-to-Many)
-- ============================================================================
CREATE TABLE issue_label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    issue_id INTEGER NOT NULL,
    label_id INTEGER NOT NULL,
    FOREIGN KEY (issue_id) REFERENCES issue(id) ON DELETE CASCADE,
    FOREIGN KEY (label_id) REFERENCES label(id) ON DELETE CASCADE,
    UNIQUE(issue_id, label_id)
);

-- Issue Assignees
-- ============================================================================
CREATE TABLE issue_assignee (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    issue_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    assigned_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (issue_id) REFERENCES issue(id) ON DELETE CASCADE,
    UNIQUE(issue_id, user_id)
);

-- Pull Requests
-- ============================================================================
CREATE TABLE pull_request (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    pr_number INTEGER NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    state VARCHAR(20) DEFAULT 'open',
    source_branch VARCHAR(200) NOT NULL,
    target_branch VARCHAR(200) NOT NULL,
    author_id INTEGER NOT NULL,
    assignee_id INTEGER,
    milestone_id INTEGER,
    is_draft INTEGER DEFAULT 0,
    is_mergeable INTEGER DEFAULT 1,
    conflicts_count INTEGER DEFAULT 0,
    files_changed_count INTEGER DEFAULT 0,
    additions_count INTEGER DEFAULT 0,
    deletions_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    review_required INTEGER DEFAULT 0,
    approved_count INTEGER DEFAULT 0,
    changes_requested_count INTEGER DEFAULT 0,
    merged_by INTEGER,
    merged_at TEXT,
    closed_by INTEGER,
    closed_at TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (milestone_id) REFERENCES milestone(id) ON DELETE SET NULL,
    UNIQUE(repository_id, pr_number)
);

-- Pull Request Labels
-- ============================================================================
CREATE TABLE pull_request_label (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pull_request_id INTEGER NOT NULL,
    label_id INTEGER NOT NULL,
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE,
    FOREIGN KEY (label_id) REFERENCES label(id) ON DELETE CASCADE,
    UNIQUE(pull_request_id, label_id)
);

-- Pull Request Reviewers
-- ============================================================================
CREATE TABLE pull_request_reviewer (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pull_request_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    reviewed_at TEXT,
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE,
    UNIQUE(pull_request_id, user_id)
);

-- Pull Request File Changes
-- ============================================================================
CREATE TABLE pull_request_file_change (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pull_request_id INTEGER NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    change_type VARCHAR(20) NOT NULL,
    additions INTEGER DEFAULT 0,
    deletions INTEGER DEFAULT 0,
    old_path VARCHAR(1000),
    diff_content TEXT,
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE
);

-- Comments
-- ============================================================================
CREATE TABLE comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(20) NOT NULL,
    entity_id INTEGER NOT NULL,
    parent_comment_id INTEGER,
    author_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    is_edited INTEGER DEFAULT 0,
    edited_at TEXT,
    reaction_count INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_comment_id) REFERENCES comment(id) ON DELETE CASCADE
);

-- Stars
-- ============================================================================
CREATE TABLE star (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    starred_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, user_id)
);

-- Activity / Events
-- ============================================================================
CREATE TABLE activity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(20),
    entity_id INTEGER,
    metadata TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Notifications
-- ============================================================================
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    repository_id INTEGER,
    notification_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(20),
    entity_id INTEGER,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read INTEGER DEFAULT 0,
    read_at TEXT,
    url VARCHAR(500),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Indexes for Performance
-- ============================================================================
CREATE INDEX idx_repo_owner ON repository(owner_id);
CREATE INDEX idx_repo_org ON repository(organization_id);
CREATE INDEX idx_repo_visibility ON repository(visibility);
CREATE INDEX idx_file_repo_branch ON repository_file(repository_id, branch_id);
CREATE INDEX idx_file_parent ON repository_file(parent_id);
CREATE INDEX idx_file_path ON repository_file(path);
CREATE INDEX idx_issue_repo ON issue(repository_id);
CREATE INDEX idx_issue_state ON issue(state);
CREATE INDEX idx_issue_assignee ON issue(assignee_id);
CREATE INDEX idx_issue_milestone ON issue(milestone_id);
CREATE INDEX idx_pr_repo ON pull_request(repository_id);
CREATE INDEX idx_pr_state ON pull_request(state);
CREATE INDEX idx_pr_author ON pull_request(author_id);
CREATE INDEX idx_comment_entity ON comment(entity_type, entity_id);
CREATE INDEX idx_activity_repo ON activity(repository_id);
CREATE INDEX idx_activity_user ON activity(user_id);
CREATE INDEX idx_notification_user ON notification(user_id, is_read);
CREATE INDEX idx_custom_field_entity ON custom_field_definition(entity_type);

-- Minimal Sample Data
-- ============================================================================

-- Sample Organization
INSERT INTO organization (name, display_name, description) VALUES
('demo-org', 'Demo Organization', 'Sample organization');

-- Sample Repository
INSERT INTO repository (name, slug, description, owner_id, visibility, language, readme_content) VALUES
('demo-repo', 'demo-repo', 'Sample repository', 1, 'public', 'Java', '# Demo Repository\n\nThis is a sample repository.');

-- Sample Branch
INSERT INTO branch (repository_id, name, is_default, is_protected, created_by) VALUES
(1, 'main', 1, 1, 1);

-- Sample File
INSERT INTO repository_file (repository_id, branch_id, path, name, is_directory, content, language) VALUES
(1, 1, '/README.md', 'README.md', 0, '# Demo Repository\n\nThis is a sample repository.', 'Markdown');

-- Sample Labels
INSERT INTO label (repository_id, name, color, description) VALUES
(1, 'bug', '#d73a4a', 'Something is not working'),
(1, 'enhancement', '#a2eeef', 'New feature or request');

-- Sample Milestone
INSERT INTO milestone (repository_id, title, description, state) VALUES
(1, 'v1.0', 'First release', 'open');

-- Sample Issue
INSERT INTO issue (repository_id, issue_number, title, description, state, author_id) VALUES
(1, 1, 'Sample issue', 'This is a sample issue for testing', 'open', 1);

-- Sample Pull Request
INSERT INTO pull_request (repository_id, pr_number, title, description, state, source_branch, target_branch, author_id) VALUES
(1, 1, 'Sample PR', 'This is a sample pull request', 'open', 'feature-branch', 'main', 1);

-- Sample Custom Field Definition
INSERT INTO custom_field_definition (entity_type, field_name, field_label, field_type, description) VALUES
('repository', 'project_status', 'Project Status', 'select', 'Current status of the project'),
('issue', 'estimated_hours', 'Estimated Hours', 'number', 'Time estimate');
