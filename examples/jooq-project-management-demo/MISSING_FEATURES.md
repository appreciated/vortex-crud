# Missing Features Report

This report outlines important features currently missing from the `jooq-project-management-demo`. These have been categorized into limitations of the core framework (`vortex-crud`) and gaps in the specific demo implementation.

## 1. Core Framework Limitations

The following features are desirable for a project management tool but are currently not supported by the underlying `vortex-crud` core framework:

*   **Hierarchical / Tree View:** There is no native support for a TreeGrid or recursive view component. This prevents the proper visualization of the Task/Subtask hierarchy in the main task list (currently flat or kanban).
*   **Gantt Chart:** No specialized component exists to visualize project timelines, dependencies, and schedules in a Gantt chart format.
*   **Rich Comment Component:** The comment system currently uses a basic list view. A specialized "Chat" or "Activity Stream" component with rich text support, threading, and better UI for collaboration is missing.

## 2. Demo Implementation Gaps

These are features that *could* be implemented with the current framework but are missing from this specific demo module:

### A. Custom Fields System
*   **Missing Service Logic:** While the `custom_field_definition` table exists, the `CustomFieldService.java` is not implemented. There is no logic to dynamically parse these definitions and render corresponding form fields.
*   **No Management UI:** There is no UI to create, edit, or delete custom field definitions.
*   **Current State:** Entities (Project, Task, etc.) currently just have a raw JSON text area for `custom_fields`.

### B. Project Dashboard
*   **Visualization:** The "Dashboard" is currently just a grid view of projects.
*   **Charts:** Missing analytical components such as:
    *   Burn-down charts.
    *   Velocity tracking.
    *   Task distribution by status/assignee.
    *   Budget/Time utilization graphs.

### C. Sprint / Iteration Management
*   **Schema:** There is no `sprint` table in the database schema.
*   **Logic:** The system supports `milestones`, but lacks true Scrum-style sprint management (start/end dates, active status, moving unfinished tasks).
*   **UI:** No board view specific to an active sprint.

### D. Time Tracking & Reporting
*   **Aggregation:** `TimeEntry` records can be created, but there are no reports to aggregate hours by User, Project, or Task.
*   **Billable vs. Non-billable:** No distinction in the schema or logic.

### E. Advanced Task Management
*   **Subtask Visualization:** Subtasks are only visible within the parent Task's form. There is no easy way to see the full hierarchy or expand/collapse tasks in the main list.
*   **Dependencies:** While `parent_task_id` exists, there is no support for blocking/blocked-by relationships (e.g., "Task B cannot start until Task A is done").

### F. Notifications
*   **In-App Only:** Notifications are only stored in the database. No integration with Email (SMTP) or external systems (Slack/Teams hooks).
