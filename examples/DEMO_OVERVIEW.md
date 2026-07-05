# Vortex CRUD Demo Applications

Two simplified demo applications showcasing the Vortex CRUD framework with custom fields support using jOOQ.

## Demos

### 1. Project Management Platform
**Port:** 8081 | **Path:** `jooq-project-management-demo/`

Features: Projects, Tasks, Milestones, Labels, Comments, Custom Fields

**Database:** 8 tables - `custom_field_definition`, `project`, `project_member`, `milestone`, `label`, `task`, `task_label`, `task_comment`

### 2. Development Platform
**Port:** 8082 | **Path:** `jooq-dev-platform-demo/`

Features: Repositories, Organizations, Issues, Pull Requests, Milestones, Labels, Comments, Custom Fields

**Database:** 12 tables - `custom_field_definition`, `organization`, `organization_member`, `repository`, `repository_collaborator`, `label`, `milestone`, `issue`, `issue_label`, `pull_request`, `pull_request_label`, `comment`

## Custom Fields System

Both demos implement a flexible custom fields architecture using JSON storage and meta tables.

### Architecture

```
┌──────────────────────┐         ┌──────────────────────┐
│ custom_field_        │         │   Entity Tables      │
│   definition         │────────▶│                      │
│                      │         │ • id                 │
│ • entity_type        │         │ • name               │
│ • field_name         │         │ • custom_fields ◄────┤
│ • field_label        │         │   (JSON)             │
│ • field_type         │         └──────────────────────┘
│ • options (JSON)     │
│ • validation_rules   │
└──────────────────────┘
```

### Meta Table Schema

```sql
CREATE TABLE custom_field_definition (
    entity_type VARCHAR(50) NOT NULL,    -- 'project', 'task', 'issue'
    field_name VARCHAR(100) NOT NULL,    -- Database identifier
    field_label VARCHAR(100) NOT NULL,   -- UI display name
    field_type VARCHAR(50) NOT NULL,     -- text, number, date, select, checkbox
    options TEXT,                        -- JSON: ["Option 1", "Option 2"]
    validation_rules TEXT,               -- JSON: {"min": 0, "max": 100}
    is_required BOOLEAN,
    UNIQUE(entity_type, field_name)
);
```

### Entity Storage

```sql
CREATE TABLE project (
    id INTEGER PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    custom_fields TEXT  -- JSON: {"client_name": "ACME", "contract_value": 50000}
);
```

### Supported Field Types

- **text** - Single-line text input
- **number** - Numeric input with validation
- **date** - Date picker
- **select** - Dropdown (options in JSON)
- **multiselect** - Multi-select dropdown
- **checkbox** - Boolean toggle

### Example Usage

**Define custom field:**
```sql
INSERT INTO custom_field_definition
(entity_type, field_name, field_label, field_type, is_required)
VALUES
('project', 'client_name', 'Client Name', 'text', 1);
```

**Store value:**
```sql
UPDATE project
SET custom_fields = '{"client_name": "ACME Corporation"}'
WHERE id = 1;
```

**Query by custom field (SQLite):**
```sql
SELECT * FROM project
WHERE json_extract(custom_fields, '$.client_name') = 'ACME Corporation';
```

### Benefits

✅ No schema migrations needed
✅ Unlimited custom fields per entity
✅ Type-safe validation
✅ Multi-tenant friendly
✅ Dynamic UI generation

## Getting Started

```bash
cd examples/jooq-project-management-demo   # or jooq-dev-platform-demo
mvn clean generate-sources                  # Generate jOOQ classes
mvn spring-boot:run                         # Run application
```

## Technology

- **jOOQ 3.19.22** - Type-safe SQL queries with enum support
- **SQLite 3.47.1.0** - Database with JSON support
- **Liquibase 4.29.0** - Database migrations
- **Spring Boot**
- **Java 21**

## Implementation Status

- Both demos have full configuration providers with routes for all entities.
- The project management demo implements the custom fields system end-to-end using the core
  **dynamic fields** feature (`DynamicFieldsConfiguration` on `FormRoute`): definitions are
  managed at runtime via the `custom-fields` route and render as real typed form fields on
  projects, tasks and milestones.
- The project management demo additionally showcases a **database-defined kanban workflow**
  (`DynamicKanbanWorkflow` + `workflow_transition` table, manageable via the `workflow` route).
- The dev platform demo still exposes `custom_fields` as raw JSON; wiring it to
  `DynamicFieldsConfiguration` works the same way as in the project management demo.

See individual README files in each demo directory for details.
