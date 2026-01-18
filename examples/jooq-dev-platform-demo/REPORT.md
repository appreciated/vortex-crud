# Missing Features Report: jooq-dev-platform-demo

This report outlines the critical gaps and important missing features in the `jooq-dev-platform-demo` application. While the current implementation provides a solid structural foundation (database schema, basic routes, and entities), significant functionality required for a usable development platform is currently missing.

## 1. Critical Missing Features

### 1.1 Deep Git Integration
The current `GitService` and UI are superficial.
- **Branch Management**: No ability to create, delete, or switch branches in the UI. The code browser defaults to `HEAD`.
- **Commit History**: While a route exists, there is no visual commit graph or detailed commit view (diffs per commit).
- **Diff Viewer**: This is the most critical gap for a dev platform. There is no way to view file changes (diffs) between commits or branches.
- **Blame/Annotate**: No ability to see line-by-line attribution in the file viewer.
- **Raw File Access**: No way to view or download the raw content of a file.

### 1.2 Pull Request Review Experience
The Pull Request feature is currently limited to metadata (title, description, state).
- **Code Review Interface**: No "Files Changed" tab in PRs to view the diff between source and target branches.
- **Inline Comments**: No ability to comment on specific lines of code within a PR.
- **Merge Conflict Detection**: No logic to check if a PR can be merged cleanly.
- **Merge Actions**: No button to actually perform the merge operation (Fast-forward, Squash, Merge Commit).

### 1.3 CI/CD Pipelines
There is no infrastructure for Continuous Integration/Deployment.
- **Pipeline Configuration**: No parsing of `.gitlab-ci.yml` or `.github/workflows`.
- **Runners**: No concept of build runners or agents.
- **Build Logs**: No UI to view real-time or archived build logs.
- **Artifacts**: No storage or retrieval of build artifacts.

### 1.4 Advanced Search
The current search is limited to database entities (finding a repo by name).
- **Code Search**: No ability to search for string occurrences within the Git repositories itself.
- **Elasticsearch/Lucene Integration**: Likely required for performance on large codebases.

## 2. Important Functional Enhancements

### 2.1 User Settings & Security
- **SSH/GPG Keys**: Users cannot manage SSH keys for git operations or GPG keys for commit signing.
- **2FA**: No Two-Factor Authentication setup.
- **Personal Access Tokens**: No management for API tokens.

### 2.2 Dashboard & Activity
- **Activity Feed**: The dashboard is currently a placeholder. It needs a global feed of events (pushes, forks, stars, comments) from followed users and repositories.
- **"Watch" Functionality**: While "Starring" is implemented, "Watching" (subscribing to notifications) is missing from the UI logic.

### 2.3 Webhooks & Integrations
- **Webhooks**: No system to trigger external HTTP requests on repository events (push, issue created, etc.).
- **Service Integrations**: No native integrations (Slack, Discord, etc.).

### 2.4 Wiki & Documentation
- **Markdown Editor**: The wiki edit experience is likely a basic text area. A proper Markdown editor with preview is needed.
- **History**: Wiki pages usually have their own git history, which is not exposed.

### 2.5 Snippets (Gists)
- **Code Snippets**: A feature to share single files or small scripts outside of a full repository context is missing.

## 3. Core Framework Gaps
As noted in the README, the demo works around some limitations in the core library:
- **Native Custom Fields**: Currently using a manual JSON approach. Native EAV or JSON support in the core would simplify this.
- **RBAC DSL**: The security configuration is verbose (`JoinTableRoleResolutionStrategy`). A simplified DSL for defining permissions would be beneficial.
