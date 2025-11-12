-- ============================================================================
-- VORTEX-CRUD: Development Platform Demo (GitHub/GitLab-like)
-- Database Schema Design (SQLite with jOOQ)
-- ============================================================================

-- Custom Fields Meta System
-- ============================================================================
CREATE TABLE custom_field_definition (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,          -- 'repository', 'issue', 'pull_request'
    field_name VARCHAR(100) NOT NULL,
    field_label VARCHAR(100) NOT NULL,
    field_type VARCHAR(50) NOT NULL,           -- 'text', 'number', 'date', 'select', 'multiselect', 'checkbox'
    field_order INTEGER DEFAULT 0,
    is_required BOOLEAN DEFAULT FALSE,
    default_value TEXT,
    options TEXT,                               -- JSON array for select/multiselect
    validation_rules TEXT,                      -- JSON: {"min": 0, "max": 100, "pattern": "regex"}
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    custom_fields TEXT
);

CREATE TABLE organization_member (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    organization_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    role VARCHAR(50) DEFAULT 'member',          -- 'owner', 'admin', 'member'
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    UNIQUE(organization_id, user_id)
);

-- Repositories
-- ============================================================================
CREATE TABLE repository (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,                 -- URL-friendly name
    description TEXT,
    owner_id INTEGER,                           -- User ID (personal repo)
    organization_id INTEGER,                    -- Org ID (org repo)
    visibility VARCHAR(20) DEFAULT 'private',   -- 'public', 'private', 'internal'
    default_branch VARCHAR(100) DEFAULT 'main',
    is_template BOOLEAN DEFAULT FALSE,
    is_archived BOOLEAN DEFAULT FALSE,
    is_fork BOOLEAN DEFAULT FALSE,
    parent_repository_id INTEGER,               -- If forked
    star_count INTEGER DEFAULT 0,
    fork_count INTEGER DEFAULT 0,
    open_issues_count INTEGER DEFAULT 0,
    open_prs_count INTEGER DEFAULT 0,
    size_bytes BIGINT DEFAULT 0,
    language VARCHAR(50),                       -- Primary language
    topics TEXT,                                -- JSON array: ["javascript", "react"]
    homepage_url VARCHAR(255),
    license VARCHAR(50),
    readme_content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_repository_id) REFERENCES repository(id) ON DELETE SET NULL,
    UNIQUE(owner_id, slug),
    UNIQUE(organization_id, slug)
);

-- Repository Members (Collaborators)
-- ============================================================================
CREATE TABLE repository_collaborator (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    permission VARCHAR(50) DEFAULT 'read',      -- 'read', 'write', 'admin'
    invited_by INTEGER,
    invited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, user_id)
);

-- Branches (virtual, no actual git)
-- ============================================================================
CREATE TABLE branch (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    is_protected BOOLEAN DEFAULT FALSE,
    created_from_branch_id INTEGER,
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_commit_at TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (created_from_branch_id) REFERENCES branch(id) ON DELETE SET NULL,
    UNIQUE(repository_id, name)
);

-- Files / Directories (file system simulation)
-- ============================================================================
CREATE TABLE repository_file (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    branch_id INTEGER NOT NULL,
    path VARCHAR(1000) NOT NULL,                -- Full path: /src/components/Header.tsx
    name VARCHAR(255) NOT NULL,
    parent_id INTEGER,                          -- Parent directory
    is_directory BOOLEAN DEFAULT FALSE,
    content TEXT,                               -- File content (for text files)
    size_bytes BIGINT DEFAULT 0,
    mime_type VARCHAR(100),
    language VARCHAR(50),                       -- Programming language
    encoding VARCHAR(20) DEFAULT 'utf-8',
    sha_hash VARCHAR(64),                       -- Simulated content hash
    created_by INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by INTEGER,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    color VARCHAR(20) NOT NULL,                 -- Hex color
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    state VARCHAR(20) DEFAULT 'open',           -- 'open', 'closed'
    due_date DATE,
    open_issues_count INTEGER DEFAULT 0,
    closed_issues_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Issues
-- ============================================================================
CREATE TABLE issue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    issue_number INTEGER NOT NULL,              -- Auto-increment per repo: #1, #2
    title VARCHAR(300) NOT NULL,
    description TEXT,
    state VARCHAR(20) DEFAULT 'open',           -- 'open', 'closed'
    state_reason VARCHAR(50),                   -- 'completed', 'not_planned', 'reopened'
    issue_type VARCHAR(50) DEFAULT 'issue',     -- 'bug', 'enhancement', 'question', 'documentation'
    priority VARCHAR(50) DEFAULT 'medium',      -- 'low', 'medium', 'high', 'critical'
    author_id INTEGER NOT NULL,
    assignee_id INTEGER,
    milestone_id INTEGER,
    locked BOOLEAN DEFAULT FALSE,
    closed_by INTEGER,
    closed_at TIMESTAMP,
    comment_count INTEGER DEFAULT 0,
    reaction_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

-- Issue Assignees (Many-to-Many, supporting multiple assignees)
-- ============================================================================
CREATE TABLE issue_assignee (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    issue_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (issue_id) REFERENCES issue(id) ON DELETE CASCADE,
    UNIQUE(issue_id, user_id)
);

-- Pull Requests
-- ============================================================================
CREATE TABLE pull_request (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    pr_number INTEGER NOT NULL,                 -- Auto-increment per repo: #1, #2
    title VARCHAR(300) NOT NULL,
    description TEXT,
    state VARCHAR(20) DEFAULT 'open',           -- 'open', 'merged', 'closed'
    source_branch VARCHAR(200) NOT NULL,
    target_branch VARCHAR(200) NOT NULL,
    author_id INTEGER NOT NULL,
    assignee_id INTEGER,
    milestone_id INTEGER,
    is_draft BOOLEAN DEFAULT FALSE,
    is_mergeable BOOLEAN DEFAULT TRUE,
    conflicts_count INTEGER DEFAULT 0,
    files_changed_count INTEGER DEFAULT 0,
    additions_count INTEGER DEFAULT 0,
    deletions_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    review_required BOOLEAN DEFAULT FALSE,
    approved_count INTEGER DEFAULT 0,
    changes_requested_count INTEGER DEFAULT 0,
    merged_by INTEGER,
    merged_at TIMESTAMP,
    closed_by INTEGER,
    closed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    custom_fields TEXT,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (milestone_id) REFERENCES milestone(id) ON DELETE SET NULL,
    UNIQUE(repository_id, pr_number)
);

-- Pull Request Labels (Many-to-Many)
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
    status VARCHAR(50) DEFAULT 'pending',       -- 'pending', 'approved', 'changes_requested', 'commented'
    reviewed_at TIMESTAMP,
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE,
    UNIQUE(pull_request_id, user_id)
);

-- Pull Request File Changes (simulated diff)
-- ============================================================================
CREATE TABLE pull_request_file_change (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pull_request_id INTEGER NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    change_type VARCHAR(20) NOT NULL,           -- 'added', 'modified', 'deleted', 'renamed'
    additions INTEGER DEFAULT 0,
    deletions INTEGER DEFAULT 0,
    old_path VARCHAR(1000),                     -- For renames
    diff_content TEXT,                          -- Simulated diff
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE
);

-- Comments (unified for Issues and PRs)
-- ============================================================================
CREATE TABLE comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(20) NOT NULL,           -- 'issue', 'pull_request', 'commit'
    entity_id INTEGER NOT NULL,
    parent_comment_id INTEGER,                  -- For nested replies
    author_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    is_edited BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP,
    reaction_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_comment_id) REFERENCES comment(id) ON DELETE CASCADE
);

-- Code Review Comments (inline comments on PR diffs)
-- ============================================================================
CREATE TABLE review_comment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pull_request_id INTEGER NOT NULL,
    file_change_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    line_number INTEGER,
    content TEXT NOT NULL,
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_by INTEGER,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pull_request_id) REFERENCES pull_request(id) ON DELETE CASCADE,
    FOREIGN KEY (file_change_id) REFERENCES pull_request_file_change(id) ON DELETE CASCADE
);

-- Reactions (GitHub-style emoji reactions)
-- ============================================================================
CREATE TABLE reaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(20) NOT NULL,           -- 'issue', 'pull_request', 'comment'
    entity_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,         -- 'thumbs_up', 'thumbs_down', 'laugh', 'heart', 'rocket', 'eyes'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(entity_type, entity_id, user_id, reaction_type)
);

-- Watchers / Subscribers
-- ============================================================================
CREATE TABLE watcher (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(20) NOT NULL,           -- 'repository', 'issue', 'pull_request'
    entity_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    watch_type VARCHAR(20) DEFAULT 'watching',  -- 'watching', 'ignoring', 'mentioned_only'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(entity_type, entity_id, user_id)
);

-- Stars / Favorites
-- ============================================================================
CREATE TABLE star (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    starred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, user_id)
);

-- Activity / Events
-- ============================================================================
CREATE TABLE activity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    action_type VARCHAR(50) NOT NULL,           -- 'opened', 'closed', 'merged', 'commented', 'pushed'
    entity_type VARCHAR(20),                    -- 'issue', 'pull_request', 'commit'
    entity_id INTEGER,
    metadata TEXT,                              -- JSON: {"commits": 5, "branch": "main"}
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Notifications
-- ============================================================================
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    repository_id INTEGER,
    notification_type VARCHAR(50) NOT NULL,     -- 'issue_assigned', 'pr_review_requested', 'comment_mention'
    entity_type VARCHAR(20),
    entity_id INTEGER,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE
);

-- Webhooks (for extensibility)
-- ============================================================================
CREATE TABLE webhook (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    url VARCHAR(500) NOT NULL,
    secret VARCHAR(100),
    events TEXT,                                -- JSON array: ["push", "pull_request", "issues"]
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_triggered_at TIMESTAMP,
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
