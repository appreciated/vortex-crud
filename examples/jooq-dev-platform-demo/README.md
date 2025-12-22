# Development Platform Demo

A simplified GitHub/GitLab-style platform demo built with Vortex CRUD and jOOQ, showcasing custom fields support.

## Features

- **Repositories**: Code repository management with visibility controls
- **Organizations**: Team structure
- **Issues**: Bug tracking and feature requests
- **Pull Requests**: Code review workflow
- **Milestones**: Release planning
- **Labels**: Flexible categorization
- **Comments**: Collaboration on issues and PRs
- **Custom Fields**: User-defined fields with JSON storage

## Database Schema (12 tables)

Core tables: `custom_field_definition`, `organization`, `organization_member`, `repository`, `repository_collaborator`, `label`, `milestone`, `issue`, `issue_label`, `pull_request`, `pull_request_label`, `comment`

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
cd examples/jooq-dev-platform-demo
mvn clean generate-sources  # Generate jOOQ classes
mvn spring-boot:run          # Run application
```

Access: http://localhost:8082

## Next Steps

1. Implement `CustomFieldService.java` for dynamic field handling
2. Add UI components for custom field management

## Missing Features in Core

The following features are currently missing in the Vortex CRUD core but would greatly benefit this demo:

- **Native Custom Fields Support**: First-class support for defining and rendering dynamic fields (EAV or JSON-based) without custom implementation overhead.
- **Auto-Enum Mapping**: Automatic generation of Select options from Java Enums or jOOQ Enum types.
- **Simplified RBAC**: More concise configuration for Role Resolution Strategies, inferring relationships from DataStore configuration.

## High Priority Tasks

Features that would be expected in a full implementation:

- **"Star" & "Watch" Actions**: Ability to favorite repositories and subscribe to notifications.
- **Activity Dashboard**: A feed showing recent commits, issues, and comments.
- **Git Integration**: Actual interface with Git repositories for code browsing and diffs.
- **Wiki**: Documentation pages for repositories.
- **Notifications**: A system to notify users of mention or updates.

See [DEMO_OVERVIEW.md](../DEMO_OVERVIEW.md) for architecture details.
