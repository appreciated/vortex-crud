# Missing Features Report for jOOQ Development Platform Demo

This report analyzes the current state of the `jooq-dev-platform-demo` module, highlighting important missing features, limitations in the current implementation, and dependencies on missing Core framework capabilities.

## 1. Missing Core Framework Features

These are features missing in the `vortex-crud` core that force verbose or suboptimal implementations in this demo.

### 1.1 Native Custom Fields Support
*   **Current State**: Custom fields are stored as JSON strings in `TEXT` columns (e.g., `ORGANIZATION.CUSTOM_FIELDS`, `REPOSITORY.CUSTOM_FIELDS`).
*   **Implementation**: In `DevPlatformConfiguration.java`, they are mapped as simple `JooqTextAreaField`:
    ```java
    Map.entry(ISSUE.CUSTOM_FIELDS, JooqTextAreaField.builder().build())
    ```
*   **Missing**: A true EAV (Entity-Attribute-Value) or JSON-schema driven UI component that allows defining fields (Text, Number, Date, Select) dynamically and rendering them as proper form inputs, rather than a raw JSON text area.
*   **Impact**: Users cannot easily manage custom fields without manually editing JSON.

### 1.2 Auto-Enum Mapping
*   **Current State**: Enums require manual mapping between Java Enums, database values, and i18n keys.
*   **Implementation**:
    ```java
    LinkedHashMap<IssueState, String> issueStates = new LinkedHashMap<>();
    issueStates.put(IssueState.OPEN, "selects.issue-state.open");
    // ...
    Map.entry(ISSUE.STATE, JooqSelectField.builder().values("issue-state").build())
    ```
*   **Missing**: Automatic discovery where the framework infers the options from the Java Enum or jOOQ Enum definition and generates the selection list automatically.
*   **Impact**: Increased boilerplate code in configuration.

### 1.3 Simplified RBAC Configuration
*   **Current State**: Role resolution requires complex, verbose strategies.
*   **Implementation**:
    ```java
    new ClassBasedRoleResolutionStrategy<>(
        Map.of(
            REPOSITORY.getRecordType(), new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                (VortexCrudDataStore) repositoryCollaboratorStore,
                REPOSITORY_COLLABORATOR.USER_ID, ...
            )
        )
    )
    ```
*   **Missing**: A streamlined API to define ownership and permission relationships, potentially inferring them from the `JooqReferenceField` configuration or Foreign Keys.

## 2. Missing Functional Features in Demo

These are features expected in a "GitHub-like" platform that are partially implemented or missing in the demo code.

### 2.1 Advanced Git Integration
*   **Current State**: `GitService.java` provides rudimentary file system interaction. It creates dummy files if missing and lists files.
*   **Missing**:
    *   Real Git backend interaction (commits, push/pull, branching, merging).
    *   Diff viewer for commits and pull requests.
    *   Commit history visualization (beyond a simple list of `GIT_COMMIT` records which are metadata only).
    *   Syntax highlighting in the code viewer (`RepositoryDetailView` just uses a `<pre>` block).

### 2.2 Wiki System
*   **Current State**: `RepositoryDetailView` lists wiki pages and dumps content.
*   **Missing**:
    *   Markdown rendering for wiki content.
    *   In-place editing interface for Wiki pages within the repository view.
    *   Version history for wiki pages.

### 2.3 Repository "Watch" Feature
*   **Current State**: Users can "Star" a repository (toggle implemented in `RepositoryDetailView`).
*   **Missing**: "Watch" functionality to subscribe to notifications. Currently, notifications are triggered via hooks in `DevPlatformConfiguration` based on assignees/owners, but not subscriptions.

### 2.4 Activity Dashboard
*   **Current State**: `DashboardView` shows "Assigned Issues" and "My Pull Requests".
*   **Missing**: A global or project-based Activity Feed showing a timeline of events (commits, comments, closed issues, etc.), as mentioned in the README "High Priority Tasks".

### 2.5 Code Review & Diffing
*   **Current State**: Pull Requests are just metadata records with a status.
*   **Missing**:
    *   Code diff view between Source and Target branches.
    *   Inline commenting on code lines.
    *   Merge conflict detection (logic is missing in service layer).

### 2.6 Dynamic Custom Field Service
*   **Current State**: The README mentions implementing `CustomFieldService.java`.
*   **Missing**: The file `CustomFieldService.java` does not exist in the source tree. Logic to parse/validate the JSON custom fields is absent.

## 3. Summary of Recommendations

1.  **Enhance `GitService`**: Integrate JGit more deeply to support real repository operations and read actual commit history/diffs.
2.  **Implement Custom Fields UI**: Create a custom form element that parses the JSON structure and renders appropriate dynamic inputs.
3.  **Improve Views**:
    *   Add Markdown rendering to Wiki and README views.
    *   Add Syntax Highlighting to Code View.
    *   Implement the Activity Feed in `DashboardView`.
4.  **Refactor Configuration**: As Core features become available, refactor `DevPlatformConfiguration` to use auto-enum mapping and simplified RBAC to reduce code size.
