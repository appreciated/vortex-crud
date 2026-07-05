# Project Management Platform Demo

A simplified project management platform demo built with Vortex CRUD and jOOQ, showcasing dynamic (custom) fields and a database-defined kanban workflow.

## Features

- **Projects**: Project tracking with progress, budget, timelines
- **Tasks**: Task management with types (task, bug, story, epic, subtask)
- **Milestones**: Delivery tracking
- **Labels**: Flexible categorization, manageable via the `labels` route
- **Comments**: Basic collaboration
- **Dynamic (Custom) Fields**: User-defined fields with JSON storage, rendered as real typed form fields
- **Kanban Workflow**: Jira-like status transition rules, defined in the database

## Database Schema (11 tables)

Core tables: `custom_field_definition`, `workflow_transition`, `project`, `project_member`, `milestone`, `label`, `task`, `task_label`, `task_comment`, `time_entry`, `attachment`

## Dynamic (Custom) Fields

Uses the core `DynamicFieldsConfiguration` feature (see `core/.../config/model/DynamicFieldsConfiguration.java`):

- `custom_field_definition` stores the field metadata (entity type, name, label, type, options, order, required, active)
- Entity tables (`project`, `task`, `milestone`) store the values in a `custom_fields` JSON column
- At form render time each definition row is materialized into the framework's actual field model type
  and bound with its actual Java value type (`String`, `Double`, `LocalDate`, `Boolean`, `Set<String>`)
- Supported types: text, textarea, number, date, select, multiselect, checkbox
- Definitions are manageable at runtime via the `custom-fields` route — no migration or redeploy needed

Configured in `ProjectManagementConfiguration` via `JooqFormRoute.builder()...dynamicFields(...)`.

## Kanban Workflow

Uses the core `KanbanWorkflow` feature: the kanban boards only allow status transitions that are
declared in the `workflow_transition` table (`DynamicKanbanWorkflow`). Invalid target columns don't
accept drops, and rejected transitions show a notification. The flow is manageable at runtime via
the `workflow` route. A `StaticKanbanWorkflow` variant exists for defining the flow in code.

## Technology

- jOOQ 3.19.22 (type-safe SQL with enum support)
- SQLite 3.47.1.0
- Liquibase 4.29.0
- Spring Boot
- Java 21

## Getting Started

```bash
cd examples/jooq-project-management-demo
mvn clean generate-sources  # Generate jOOQ classes
mvn spring-boot:run          # Run application
```

Access: http://localhost:8081

## Missing Features in Core

The following features are currently missing in the Vortex CRUD core but would greatly benefit this demo:

- **Hierarchical/Tree View**: Support for TreeGrid or recursive views to visualize Tasks and Subtasks nested structure.
- **Gantt Chart**: A dedicated Gantt chart component for visualizing project timelines and dependencies.
- **Rich Comment Component**: A specialized timeline/chat UI component for comments, replacing the basic list view.

## High Priority Tasks

Features that would be expected in a full implementation:

- **Project Dashboard**: Visualizations like Burn-down charts and velocity tracking.
- **Subtasks UI**: Support for parent/child task selection and visualization.
- **Rich Text Descriptions**: Markdown support for task descriptions.

See [DEMO_OVERVIEW.md](../DEMO_OVERVIEW.md) for architecture details.
