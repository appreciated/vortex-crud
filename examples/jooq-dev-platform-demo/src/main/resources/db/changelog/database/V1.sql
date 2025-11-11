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

-- Sample Data
-- ============================================================================

-- Sample Organizations
INSERT INTO organization (name, display_name, description, website) VALUES
('vortex-team', 'Vortex Team', 'Open source development team', 'https://vortex.dev'),
('acme-corp', 'ACME Corporation', 'Enterprise software solutions', 'https://acme.com');

-- Sample Repositories
INSERT INTO repository (name, slug, description, owner_id, organization_id, visibility, language, topics, readme_content, star_count, fork_count) VALUES
('vortex-crud', 'vortex-crud', 'A powerful CRUD framework for Vaadin applications with advanced features', 1, 1, 'public', 'Java', '["java", "vaadin", "crud", "framework"]', '# Vortex CRUD\n\nA modern CRUD framework for building powerful data management applications.\n\n## Features\n- Multiple view types (Grid, List, Kanban, Calendar)\n- Custom fields support\n- Role-based access control\n- Audit logging', 245, 42),
('mobile-app', 'mobile-app', 'Cross-platform mobile application', 1, 2, 'private', 'TypeScript', '["react-native", "mobile", "typescript"]', '# Mobile App\n\nOur flagship mobile application', 15, 3),
('api-gateway', 'api-gateway', 'Microservices API gateway', 1, 2, 'internal', 'Go', '["golang", "microservices", "api"]', '# API Gateway\n\nCentral API gateway for all services', 8, 1);

-- Sample Branches
INSERT INTO branch (repository_id, name, is_default, is_protected, created_by) VALUES
(1, 'main', 1, 1, 1),
(1, 'develop', 0, 1, 1),
(1, 'feature/custom-fields', 0, 0, 1),
(2, 'main', 1, 1, 1),
(2, 'feature/redesign', 0, 0, 1),
(3, 'main', 1, 1, 1);

-- Sample Files/Directories
INSERT INTO repository_file (repository_id, branch_id, path, name, is_directory, parent_id, language) VALUES
(1, 1, '/src', 'src', 1, NULL, NULL),
(1, 1, '/src/main', 'main', 1, 1, NULL),
(1, 1, '/src/main/java', 'java', 1, 2, NULL),
(1, 1, '/README.md', 'README.md', 0, NULL, 'Markdown'),
(1, 1, '/pom.xml', 'pom.xml', 0, NULL, 'XML');

UPDATE repository_file SET content = '# Vortex CRUD\n\nA powerful CRUD framework for Vaadin.' WHERE id = 4;
UPDATE repository_file SET content = '<?xml version="1.0"?>\n<project>\n  <modelVersion>4.0.0</modelVersion>\n</project>' WHERE id = 5;

-- Sample Labels
INSERT INTO label (repository_id, name, color, description) VALUES
(1, 'bug', '#d73a4a', 'Something is not working'),
(1, 'enhancement', '#a2eeef', 'New feature or request'),
(1, 'documentation', '#0075ca', 'Improvements or additions to documentation'),
(1, 'good-first-issue', '#7057ff', 'Good for newcomers'),
(1, 'high-priority', '#b60205', 'High priority item'),
(2, 'bug', '#d73a4a', 'Something is not working'),
(2, 'ui', '#fbca04', 'User interface related'),
(3, 'bug', '#d73a4a', 'Something is not working');

-- Sample Milestones
INSERT INTO milestone (repository_id, title, description, due_date, state, open_issues_count, closed_issues_count) VALUES
(1, 'v1.0 Release', 'First major release', '2025-03-01', 'open', 5, 12),
(1, 'v2.0 Planning', 'Next major version planning', '2025-06-01', 'open', 8, 0),
(2, 'Redesign Launch', 'Launch of new mobile design', '2025-04-15', 'open', 3, 7);

-- Sample Issues
INSERT INTO issue (repository_id, issue_number, title, description, state, issue_type, priority, author_id, assignee_id, milestone_id) VALUES
(1, 1, 'Add support for custom fields', 'We need the ability for users to define their own custom fields on entities.\n\n## Requirements\n- Meta table for field definitions\n- JSON storage for field values\n- Dynamic form generation', 'open', 'enhancement', 'high', 1, 1, 1),
(1, 2, 'Kanban board not rendering correctly', 'The kanban board shows empty columns when there are tasks.\n\n## Steps to Reproduce\n1. Navigate to tasks\n2. Switch to kanban view\n3. Observe empty columns', 'closed', 'bug', 'high', 1, 1, 1),
(1, 3, 'Add dark mode support', 'Implement dark mode theme option', 'open', 'enhancement', 'medium', 1, NULL, 2),
(1, 4, 'Improve documentation for beginners', 'Add more examples and tutorials for new users', 'open', 'documentation', 'medium', 1, NULL, 1),
(1, 5, 'Performance issue with large datasets', 'Grid view becomes slow with 10000+ records', 'open', 'bug', 'critical', 1, 1, 1),
(2, 1, 'Login screen redesign', 'Update login screen to match new brand guidelines', 'closed', 'enhancement', 'high', 1, 1, 3),
(2, 2, 'App crashes on startup', 'The app crashes immediately after opening on Android 12', 'open', 'bug', 'critical', 1, 1, 3),
(3, 1, 'Rate limiting not working', 'API rate limits are not being enforced', 'open', 'bug', 'high', 1, 1, NULL);

-- Sample Pull Requests
INSERT INTO pull_request (repository_id, pr_number, title, description, state, source_branch, target_branch, author_id, milestone_id, files_changed_count, additions_count, deletions_count) VALUES
(1, 1, 'Implement custom fields support', 'This PR adds comprehensive support for custom fields:\n- Database schema for custom field definitions\n- Service layer for CRUD operations\n- UI components for field management\n- Dynamic form rendering\n\nCloses #1', 'open', 'feature/custom-fields', 'main', 1, 1, 15, 847, 23),
(1, 2, 'Fix kanban board rendering issue', 'Fixed the bug where kanban columns were not rendering tasks correctly.\n\nThe issue was in the column grouping logic.\n\nFixes #2', 'merged', 'fix/kanban-rendering', 'main', 1, 1, 2, 45, 12),
(1, 3, 'Add dark mode theme', 'Implements dark mode support with theme toggle.\n\nRelated to #3', 'open', 'feature/dark-mode', 'develop', 1, 2, 8, 234, 45);

-- Sample Comments
INSERT INTO comment (entity_type, entity_id, author_id, content) VALUES
('issue', 1, 1, 'I''ve started working on this feature. Created a PR with the initial implementation.'),
('issue', 1, 1, 'The database schema is complete. Now working on the service layer.'),
('issue', 2, 1, 'Found the bug! It was in the column rendering logic. Fix is ready.'),
('pull_request', 1, 1, 'All tests are passing now. Ready for review.'),
('pull_request', 2, 1, 'Merged! Thanks for the quick fix.');

-- Sample PR File Changes
INSERT INTO pull_request_file_change (pull_request_id, file_path, change_type, additions, deletions) VALUES
(1, '/src/main/resources/db/changelog/database/V2.sql', 'added', 85, 0),
(1, '/src/main/java/CustomFieldService.java', 'added', 234, 0),
(1, '/src/main/java/CustomFieldController.java', 'added', 156, 0),
(2, '/src/main/java/KanbanView.java', 'modified', 23, 12),
(2, '/src/main/java/KanbanColumn.java', 'modified', 22, 0);

-- Sample PR Reviewers
INSERT INTO pull_request_reviewer (pull_request_id, user_id, status) VALUES
(1, 1, 'pending'),
(2, 1, 'approved'),
(3, 1, 'changes_requested');

-- Sample Issue Labels
INSERT INTO issue_label (issue_id, label_id) VALUES
(1, 2), -- enhancement
(1, 5), -- high-priority
(2, 1), -- bug
(2, 5), -- high-priority
(3, 2), -- enhancement
(4, 3), -- documentation
(4, 4), -- good-first-issue
(5, 1), -- bug
(6, 7), -- ui
(7, 6), -- bug (repo 2)
(8, 8); -- bug (repo 3)

-- Sample PR Labels
INSERT INTO pull_request_label (pull_request_id, label_id) VALUES
(1, 2), -- enhancement
(2, 1), -- bug
(3, 2); -- enhancement

-- Sample Stars
INSERT INTO star (repository_id, user_id) VALUES
(1, 1);

-- Sample Custom Field Definitions
INSERT INTO custom_field_definition (entity_type, field_name, field_label, field_type, field_order, is_required, options, description) VALUES
('repository', 'project_status', 'Project Status', 'select', 1, 0, '["Planning", "Active Development", "Maintenance", "Archived"]', 'Current status of the project'),
('repository', 'license_expiry', 'License Expiry Date', 'date', 2, 0, NULL, 'When the project license expires'),
('issue', 'estimated_hours', 'Estimated Hours', 'number', 1, 0, NULL, 'Estimated time to complete this issue'),
('issue', 'affected_version', 'Affected Version', 'text', 2, 0, NULL, 'Version where this issue was found'),
('pull_request', 'breaking_change', 'Breaking Change', 'checkbox', 1, 0, NULL, 'Does this PR contain breaking changes?'),
('pull_request', 'release_notes', 'Release Notes', 'text', 2, 0, NULL, 'Notes to include in release');
