# Project Management Platform Demo

A project management platform demo built with Vortex CRUD and jOOQ, showcasing custom fields support.

## Features

- **Projects**: Project tracking with progress, budget, timelines
- **Tasks**: Task management with types (task, bug, story, epic, subtask)
- **Sprints**: Agile sprint planning
- **Milestones**: Delivery tracking
- **Teams**: Team organization
- **Time Tracking**: Hour logging
- **Custom Fields**: User-defined fields with JSON storage

## Custom Fields System

Uses a meta table approach:
- `custom_field_definition` - Stores field metadata (type, validation, options)
- Entity tables have `custom_fields` JSON column for values
- Supports: text, number, date, select, multiselect, checkbox

## Database Schema

Core tables: `project`, `task`, `sprint`, `milestone`, `team`, `label`, `task_comment`, `time_entry`, `task_dependency`, `attachment`, `custom_field_definition`

## Technology

- jOOQ 3.19.22 (type-safe SQL)
- SQLite 3.47.1.0
- Liquibase 4.29.0
- Vaadin 25.0.0-beta4
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
2. Configure routes (Projects, Tasks, Sprints)
3. Implement `CustomFieldService.java` for dynamic field handling
4. Add UI components for custom field management

See [DEMO_OVERVIEW.md](../DEMO_OVERVIEW.md) for architecture details.
