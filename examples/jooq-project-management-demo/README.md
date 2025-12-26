# Project Management Platform Demo

A simplified project management platform demo built with Vortex CRUD and jOOQ, showcasing custom fields support.

## Features

- **Projects**: Project tracking with progress, budget, timelines
- **Tasks**: Task management with types (task, bug, story, epic, subtask)
- **Milestones**: Delivery tracking
- **Labels**: Flexible categorization
- **Comments**: Basic collaboration
- **Custom Fields**: User-defined fields with JSON storage

## Database Schema (10 tables)

Core tables: `custom_field_definition`, `project`, `project_member`, `milestone`, `label`, `task`, `task_label`, `task_comment`, `time_entry`, `attachment`

## Custom Fields System

Uses a meta table approach:
- `custom_field_definition` - Stores field metadata (type, validation, options)
- Entity tables have `custom_fields` JSON column for values
- Supports: text, number, date, select, multiselect, checkbox

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

## Next Steps

1. Implement `CustomFieldService.java` for dynamic field handling
2. Add UI components for custom field management

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
