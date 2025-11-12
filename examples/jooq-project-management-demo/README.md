# Project Management Platform Demo

A simplified project management platform demo built with Vortex CRUD and jOOQ, showcasing custom fields support.

## Features

- **Projects**: Project tracking with progress, budget, timelines
- **Tasks**: Task management with types (task, bug, story, epic, subtask)
- **Milestones**: Delivery tracking
- **Labels**: Flexible categorization
- **Comments**: Basic collaboration
- **Custom Fields**: User-defined fields with JSON storage

## Database Schema (8 tables)

Core tables: `custom_field_definition`, `project`, `project_member`, `milestone`, `label`, `task`, `task_label`, `task_comment`

## Custom Fields System

Uses a meta table approach:
- `custom_field_definition` - Stores field metadata (type, validation, options)
- Entity tables have `custom_fields` JSON column for values
- Supports: text, number, date, select, multiselect, checkbox

## Technology

- jOOQ 3.19.22 (type-safe SQL with enum support)
- SQLite 3.47.1.0
- Liquibase 4.29.0
- Spring Boot 4.0.0-M3
- Java 21

## Getting Started

```bash
cd examples/jooq-project-management-demo
mvn clean generate-sources  # Generate jOOQ classes
mvn spring-boot:run          # Run application
```

Access: http://localhost:8081

## Next Steps

1. Implement `ProjectManagementConfiguration.java` (Vortex CRUD config provider)
2. Configure routes (Projects, Tasks, Milestones)
3. Implement `CustomFieldService.java` for dynamic field handling
4. Add UI components for custom field management

See [DEMO_OVERVIEW.md](../DEMO_OVERVIEW.md) for architecture details.
