# Vortex CRUD Demo Applications

Two demo applications showcasing the Vortex CRUD framework with custom fields support using jOOQ.

## Demos

### 1. Project Management Platform
**Port:** 8081 | **Path:** `jooq-project-management-demo/`

Features: Projects, Tasks, Sprints, Milestones, Teams, Time Tracking, Labels

**Database:** 20+ tables including `project`, `task`, `sprint`, `milestone`, `team`, `label`, `custom_field_definition`

### 2. Development Platform
**Port:** 8082 | **Path:** `jooq-dev-platform-demo/`

Features: Repositories, Issues, Pull Requests, Branches, Files, Organizations

**Database:** 30+ tables including `repository`, `issue`, `pull_request`, `branch`, `repository_file`, `custom_field_definition`

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

- **jOOQ 3.19.22** - Type-safe SQL queries
- **SQLite 3.47.1.0** - Database with JSON support
- **Liquibase 4.29.0** - Database migrations
- **Vaadin 25.0.0-beta4** - UI framework
- **Spring Boot 4.0.0-M3**
- **Java 21**

## Next Steps

1. Implement configuration provider (`ProjectManagementConfiguration.java`)
2. Configure routes using Vortex CRUD builders
3. Implement `CustomFieldService` for dynamic field handling
4. Add UI components for custom field management

See individual README files in each demo directory for details.
