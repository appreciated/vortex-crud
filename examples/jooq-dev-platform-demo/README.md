# Development Platform Demo

A GitHub/GitLab-style development platform demo built with Vortex CRUD and jOOQ, showcasing custom fields support.

## Features

- **Repositories**: Code repository management with visibility controls
- **Organizations**: Team structure
- **Branches**: Branch management (simulated)
- **Files**: File tree browser with content storage
- **Issues**: Bug tracking and feature requests
- **Pull Requests**: Code review workflow
- **Milestones**: Release planning
- **Custom Fields**: User-defined fields with JSON storage

## Custom Fields System

Uses a meta table approach:
- `custom_field_definition` - Stores field metadata (type, validation, options)
- Entity tables have `custom_fields` JSON column for values
- Supports: text, number, date, select, multiselect, checkbox

## Database Schema

Core tables: `repository`, `organization`, `branch`, `repository_file`, `issue`, `pull_request`, `milestone`, `label`, `comment`, `pull_request_reviewer`, `star`, `activity`, `custom_field_definition`

## Technology

- jOOQ 3.19.22 (type-safe SQL)
- SQLite 3.47.1.0
- Liquibase 4.29.0
- Vaadin 25.0.0-beta4
- Spring Boot 4.0.0-M3
- Java 21

## Getting Started

```bash
cd examples/jooq-dev-platform-demo
mvn clean generate-sources  # Generate jOOQ classes
mvn spring-boot:run          # Run application
```

Access: http://localhost:8082

## Next Steps

1. Implement `DevPlatformConfiguration.java` (Vortex CRUD config provider)
2. Configure routes (Repositories, Issues, PRs)
3. Implement `CustomFieldService.java` for dynamic field handling
4. Add UI components for file browser and custom fields

See [DEMO_OVERVIEW.md](../DEMO_OVERVIEW.md) for architecture details.
