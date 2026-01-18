# Report: Missing Features in jooq-project-management-demo

This report outlines the important features currently missing from the `jooq-project-management-demo` application, based on code analysis and project documentation.

## 1. Core Project Management Features

### Project Dashboard
**Status:** Missing
**Description:** There is no dedicated dashboard view to provide a high-level overview of project health.
**Requirements:**
- **Burn-down Charts:** Visualizing work remaining vs. time.
- **Velocity Tracking:** Measuring the amount of work completed in each sprint.
- **Key Metrics:** Displaying open tasks, upcoming deadlines, and budget status.

### Interactive Gantt Chart
**Status:** Missing
**Description:** The application currently lacks a timeline view for project planning.
**Requirements:**
- A component to visualize task dependencies and schedules.
- Drag-and-drop capability to adjust start and end dates.
- Visualization of milestones on the timeline.

### Hierarchical Task View (Subtasks)
**Status:** Partially Implemented / UI Missing
**Description:** While the database supports parent/child relationships (`parent_task_id`), the UI lacks a proper tree or hierarchical view.
**Requirements:**
- **TreeGrid View:** A specialized grid that allows expanding/collapsing parent tasks to see subtasks.
- **Visual Indication:** Clear visual hierarchy in list views to distinguish between tasks and subtasks.

## 2. Functional Gaps

### Email Notifications
**Status:** Missing
**Description:** The application has an internal notification system (`NotificationRecord`), but lacks integration with external email services.
**Requirements:**
- Integration with `spring-boot-starter-mail`.
- Email templates for common events (Task Assigned, Comment Added, Sprint Started).
- User preferences to opt-in/out of specific email notifications.

### Time Tracking & Reporting
**Status:** Incomplete
**Description:** Users can log time entries (`TimeEntryRecord`) on tasks, but there is no aggregated reporting.
**Requirements:**
- **Timesheet View:** A view for users to see their logged time for a week/month.
- **Project Reports:** Aggregated time spent per project, sprint, or task type.
- **Export Functionality:** Ability to export time data for billing or analysis.

### Advanced Search & Filtering
**Status:** Basic
**Description:** A `SearchRoute` exists, but lacks advanced filtering capabilities found in mature tools.
**Requirements:**
- **Complex Queries:** Support for logical operators (AND, OR) and combining multiple criteria (e.g., "assigned to me" AND "high priority" AND "due this week").
- **Saved Filters:** Ability for users to save and reuse common search configurations.

## 3. UI/UX Improvements

### Custom Fields Management UI
**Status:** Primitive
**Description:** Custom fields are stored as JSON strings. The current UI uses a raw `JooqTextAreaField`, requiring users to manually edit JSON.
**Requirements:**
- **Form Builder:** A drag-and-drop interface to define custom fields for projects/tasks.
- **Dynamic Rendering:** Automatically rendering the appropriate input widgets (date pickers, dropdowns, etc.) based on the custom field definition, rather than a raw text area.

### Rich Comment Component
**Status:** Missing
**Description:** Comments are currently displayed in a basic list (`JooqCollection`).
**Requirements:**
- **Timeline/Chat Interface:** A modern, conversation-style layout for comments.
- **Rich Text Support:** Full support for Markdown or HTML in comments with a user-friendly editor.
- **@Mentions:** Ability to tag other users in comments.

### User Profile & Settings
**Status:** Basic
**Description:** The profile view covers basic credentials but lacks personalization.
**Requirements:**
- **Avatar Upload:** Ability for users to upload profile pictures.
- **Theme/Display Settings:** User-specific preferences for the UI.
