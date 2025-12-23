-- liquibase formatted sql

-- changeset dev-platform-demo:15
CREATE TABLE repository_star (
    user_id INTEGER NOT NULL,
    repository_id INTEGER NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(user_id, repository_id)
);

-- changeset dev-platform-demo:16
CREATE TABLE wiki_page (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, title)
);

-- changeset dev-platform-demo:17
CREATE TABLE git_commit (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    hash VARCHAR(40) NOT NULL,
    message TEXT NOT NULL,
    author_name VARCHAR(100),
    author_email VARCHAR(100),
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    UNIQUE(repository_id, hash)
);

-- changeset dev-platform-demo:18
CREATE TABLE git_branch (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    repository_id INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    head_commit_id INTEGER,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (repository_id) REFERENCES repository(id) ON DELETE CASCADE,
    FOREIGN KEY (head_commit_id) REFERENCES git_commit(id) ON DELETE SET NULL,
    UNIQUE(repository_id, name)
);

-- changeset dev-platform-demo:19
CREATE TABLE notification (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    link VARCHAR(500),
    is_read INTEGER DEFAULT 0,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- changeset dev-platform-demo:20
CREATE INDEX idx_repo_star_user ON repository_star(user_id);
CREATE INDEX idx_wiki_repo ON wiki_page(repository_id);
CREATE INDEX idx_commit_repo ON git_commit(repository_id);
CREATE INDEX idx_branch_repo ON git_branch(repository_id);
CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_notification_read ON notification(is_read);
