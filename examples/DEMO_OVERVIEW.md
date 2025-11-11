# Vortex CRUD Demo Applications Overview

This document provides a comprehensive overview of the two demo applications created to showcase the Vortex CRUD framework's capabilities, with particular emphasis on the **custom fields system** architecture.

## Created Demos

### 1. Project Management Platform (`jooq-project-management-demo`)
**Port**: 8081
**Description**: A comprehensive project management system similar to Jira, Linear, or Asana.

**Key Entities**:
- Projects, Tasks, Sprints, Milestones
- Teams, Time Tracking, Labels
- Comments, Attachments, Dependencies
- Activity Logs, Notifications

**Database**: 20+ tables with comprehensive relationships

### 2. Development Platform (`jooq-dev-platform-demo`)
**Port**: 8082
**Description**: A GitHub/GitLab-style platform for collaborative development.

**Key Entities**:
- Repositories, Organizations, Branches
- Files (simulated file tree), Issues, Pull Requests
- Labels, Milestones, Comments, Reviews
- Stars, Activity Feed, Notifications

**Database**: 30+ tables with complex relationships

## Custom Fields System Architecture

Both demos implement a sophisticated custom fields system that allows users to extend entity schemas without database migrations. This is a **critical missing feature** in many CRUD frameworks.

### Core Concept

The system uses a combination of:
1. **Meta Table**: Stores field definitions
2. **JSON Storage**: Stores actual field values
3. **Dynamic UI**: Renders forms based on definitions

This pattern is used by major platforms like Salesforce, HubSpot, and Notion.

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                   Custom Fields System                       │
└─────────────────────────────────────────────────────────────┘

┌──────────────────────┐         ┌──────────────────────┐
│ custom_field_        │         │   Entity Tables      │
│   definition         │         │  (project, task,     │
│                      │         │   issue, etc.)       │
│ • entity_type        │────────▶│                      │
│ • field_name         │         │ • id                 │
│ • field_label        │         │ • name               │
│ • field_type         │         │ • description        │
│ • is_required        │         │ • custom_fields ◄────┤
│ • options (JSON)     │         │   (TEXT/JSON)        │
│ • validation_rules   │         └──────────────────────┘
└──────────────────────┘
         │
         │ Describes
         ▼
┌──────────────────────┐
│   Custom Field       │
│   Service Layer      │
│                      │
│ • getDefinitions()   │
│ • validateValue()    │
│ • serialize()        │
│ • deserialize()      │
└──────────────────────┘
         │
         │ Renders
         ▼
┌──────────────────────┐
│   Dynamic UI         │
│   Component Factory  │
│                      │
│ • TextField          │
│ • NumberField        │
│ • DatePicker         │
│ • Select/ComboBox    │
│ • Checkbox           │
└──────────────────────┘
```

### Database Schema

#### Meta Table: `custom_field_definition`

```sql
CREATE TABLE custom_field_definition (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    entity_type VARCHAR(50) NOT NULL,          -- 'project', 'task', 'issue', etc.
    field_name VARCHAR(100) NOT NULL,          -- Database identifier
    field_label VARCHAR(100) NOT NULL,         -- UI display name
    field_type VARCHAR(50) NOT NULL,           -- Data type
    field_order INTEGER DEFAULT 0,             -- Display order
    is_required BOOLEAN DEFAULT FALSE,         -- Validation
    default_value TEXT,                        -- Default value
    options TEXT,                              -- JSON: ["Opt1", "Opt2"]
    validation_rules TEXT,                     -- JSON: {"min": 0, "max": 100}
    description TEXT,                          -- Help text
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(entity_type, field_name)
);
```

#### Entity Tables with Custom Fields

```sql
CREATE TABLE project (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    -- Standard fields...
    custom_fields TEXT,                        -- JSON storage
    -- Example value: {"client_name": "ACME", "contract_value": 50000}
);
```

### Field Types Supported

| Field Type | UI Component | Validation | Options |
|------------|--------------|------------|---------|
| `text` | TextField | Length, pattern | - |
| `number` | NumberField | Min, max, decimals | - |
| `date` | DatePicker | Min, max dates | - |
| `select` | ComboBox | Required option | JSON array |
| `multiselect` | MultiSelectComboBox | Required, max selections | JSON array |
| `checkbox` | Checkbox | - | - |

### Data Flow

#### 1. Administrator Defines Custom Field

```sql
INSERT INTO custom_field_definition
(entity_type, field_name, field_label, field_type, is_required, options)
VALUES
('project', 'client_name', 'Client Name', 'text', 1, NULL),
('project', 'project_type', 'Project Type', 'select', 0,
 '["Internal", "Client", "Open Source", "R&D"]');
```

#### 2. User Creates/Edits Entity

The form renders:
- All standard fields (name, description, etc.)
- All active custom fields for that entity type
- Appropriate UI components based on field_type
- Validation based on is_required and validation_rules

#### 3. Data is Saved

```sql
UPDATE project
SET
    name = 'New Project',
    description = 'Project description',
    custom_fields = '{
        "client_name": "ACME Corporation",
        "project_type": "Client"
    }'
WHERE id = 1;
```

#### 4. Data is Retrieved and Displayed

```java
// Read custom field definitions
List<CustomFieldDefinition> definitions =
    getDefinitionsForEntity("project");

// Read entity
Project project = projectRepository.findById(1);

// Parse JSON
Map<String, Object> customFields =
    gson.fromJson(project.getCustomFields(), Map.class);

// Render in UI
for (CustomFieldDefinition def : definitions) {
    Object value = customFields.get(def.getFieldName());
    // Create appropriate UI component
    // Set value and add to form
}
```

### Advantages of This Approach

#### ✅ Pros
1. **No Schema Changes**: Add fields without database migrations
2. **Multi-Tenancy Friendly**: Different tenants can have different fields
3. **UI Flexibility**: Fields render automatically based on metadata
4. **Type Safety**: Field types ensure proper validation
5. **Audit Trail**: Track when fields are added/modified
6. **Scalability**: Unlimited custom fields per entity
7. **Backward Compatible**: Entities without custom fields work normally

#### ⚠️ Cons (and Mitigations)
1. **Query Performance**:
   - Can't easily query/filter by custom fields in SQL
   - Mitigation: Add indexes, use JSON functions (SQLite: `json_extract`)

2. **Type Safety**:
   - JSON storage loses compile-time type checking
   - Mitigation: Service layer validation, schema validation

3. **Storage Size**:
   - JSON can be verbose
   - Mitigation: Use JSONB (PostgreSQL) or compress data

4. **Complex Queries**:
   - Reporting on custom fields is harder
   - Mitigation: Materialized views, ETL to reporting DB

### Advanced Features (To Implement)

#### 1. Field Dependencies
```json
{
    "field_name": "contract_type",
    "depends_on": {
        "field": "project_type",
        "value": "Client"
    }
}
```

#### 2. Calculated Fields
```json
{
    "field_name": "project_profit",
    "field_type": "calculated",
    "formula": "contract_value - actual_cost"
}
```

#### 3. Field Groups
```json
{
    "group_name": "Financial Information",
    "fields": ["contract_value", "budget", "actual_cost"]
}
```

#### 4. Conditional Validation
```json
{
    "field_name": "contract_value",
    "validation_rules": {
        "required_if": {
            "field": "project_type",
            "equals": "Client"
        },
        "min": 1000
    }
}
```

#### 5. Rich Field Types
- File upload
- Rich text editor
- Multi-line text
- URL with validation
- Email with validation
- Color picker
- User/team picker (reference fields)

### Implementation Guide

#### Step 1: Custom Field Service

```java
@Service
public class CustomFieldService {

    @Autowired
    private DSLContext dslContext;

    public List<CustomFieldDefinition> getActiveDefinitions(String entityType) {
        return dslContext
            .selectFrom(CUSTOM_FIELD_DEFINITION)
            .where(CUSTOM_FIELD_DEFINITION.ENTITY_TYPE.eq(entityType))
            .and(CUSTOM_FIELD_DEFINITION.IS_ACTIVE.eq(true))
            .orderBy(CUSTOM_FIELD_DEFINITION.FIELD_ORDER)
            .fetchInto(CustomFieldDefinition.class);
    }

    public Map<String, Object> parseCustomFields(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }
        return gson.fromJson(json,
            new TypeToken<Map<String, Object>>(){}.getType());
    }

    public String serializeCustomFields(Map<String, Object> fields) {
        return gson.toJson(fields);
    }

    public void validateCustomFields(
        String entityType,
        Map<String, Object> fields
    ) throws ValidationException {
        List<CustomFieldDefinition> defs =
            getActiveDefinitions(entityType);

        for (CustomFieldDefinition def : defs) {
            Object value = fields.get(def.getFieldName());

            // Check required
            if (def.getIsRequired() && value == null) {
                throw new ValidationException(
                    def.getFieldLabel() + " is required"
                );
            }

            // Validate by type
            switch (def.getFieldType()) {
                case "number":
                    validateNumber(def, value);
                    break;
                case "select":
                    validateSelect(def, value);
                    break;
                // ... other types
            }
        }
    }
}
```

#### Step 2: Dynamic Form Component Factory

```java
@Component
public class CustomFieldFormFactory {

    public Component createField(
        CustomFieldDefinition definition,
        Object currentValue
    ) {
        Component field = switch (definition.getFieldType()) {
            case "text" -> createTextField(definition, currentValue);
            case "number" -> createNumberField(definition, currentValue);
            case "date" -> createDateField(definition, currentValue);
            case "select" -> createSelectField(definition, currentValue);
            case "checkbox" -> createCheckboxField(definition, currentValue);
            default -> new TextField();
        };

        configureField(field, definition);
        return field;
    }

    private TextField createTextField(
        CustomFieldDefinition def,
        Object value
    ) {
        TextField field = new TextField(def.getFieldLabel());
        field.setValue(value != null ? value.toString() : "");
        field.setRequired(def.getIsRequired());

        // Apply validation rules
        if (def.getValidationRules() != null) {
            applyValidationRules(field, def.getValidationRules());
        }

        return field;
    }

    // Similar methods for other field types...
}
```

#### Step 3: Integration with Vortex CRUD

```java
@Service
public class ProjectManagementConfiguration
    implements VortexCrudConfigurationProvider<...> {

    @Autowired
    private CustomFieldService customFieldService;

    @Autowired
    private CustomFieldFormFactory customFieldFormFactory;

    @Override
    public Application<...> get() {

        // Standard fields
        List<InternalFormElement<...>> projectFields = new ArrayList<>();
        projectFields.add(JooqFieldElement.builder(
            PROJECT.NAME, "route.projects.labels.name"
        ).build());
        projectFields.add(JooqFieldElement.builder(
            PROJECT.DESCRIPTION, "route.projects.labels.description"
        ).build());

        // Add custom fields dynamically
        List<CustomFieldDefinition> customDefs =
            customFieldService.getActiveDefinitions("project");

        for (CustomFieldDefinition def : customDefs) {
            projectFields.add(
                createCustomFieldElement(def)
            );
        }

        FormRoute<...> projectForm = JooqFormRoute.builder()
            .dataStoreKey(/* project table */)
            .formConfiguration(JooqFormRendererConfiguration.builder()
                .titleField(PROJECT.NAME.getName())
                .children(projectFields)
                .build())
            .build();

        // ... rest of configuration
    }

    private InternalFormElement<...> createCustomFieldElement(
        CustomFieldDefinition def
    ) {
        // Create form element that:
        // 1. Reads from custom_fields JSON column
        // 2. Renders appropriate UI component
        // 3. Validates on save
        // 4. Serializes back to JSON

        // This requires extending Vortex CRUD's form system
        // to support custom field bindings
    }
}
```

### Query Examples

#### SQLite JSON Queries

```sql
-- Filter by custom field value
SELECT *
FROM project
WHERE json_extract(custom_fields, '$.client_name') = 'ACME Corporation';

-- Order by custom field
SELECT *
FROM project
ORDER BY CAST(json_extract(custom_fields, '$.contract_value') AS INTEGER) DESC;

-- Aggregate by custom field
SELECT
    json_extract(custom_fields, '$.project_type') AS project_type,
    COUNT(*) AS count,
    AVG(CAST(json_extract(custom_fields, '$.contract_value') AS REAL)) AS avg_value
FROM project
WHERE custom_fields IS NOT NULL
GROUP BY project_type;

-- Check if custom field exists
SELECT *
FROM project
WHERE json_extract(custom_fields, '$.client_name') IS NOT NULL;
```

#### jOOQ Custom Field Queries

```java
// Filter by custom field
List<Project> projects = dslContext
    .selectFrom(PROJECT)
    .where(DSL.field(
        "json_extract({0}, '$.client_name')",
        String.class,
        PROJECT.CUSTOM_FIELDS
    ).eq("ACME Corporation"))
    .fetch();

// Order by custom field
List<Project> projects = dslContext
    .selectFrom(PROJECT)
    .orderBy(DSL.field(
        "CAST(json_extract({0}, '$.contract_value') AS INTEGER)",
        Integer.class,
        PROJECT.CUSTOM_FIELDS
    ).desc())
    .fetch();
```

### Testing Strategy

#### 1. Unit Tests
```java
@Test
public void testCustomFieldValidation() {
    CustomFieldDefinition def = new CustomFieldDefinition()
        .setEntityType("project")
        .setFieldName("client_name")
        .setFieldType("text")
        .setIsRequired(true);

    Map<String, Object> fields = new HashMap<>();
    // Should throw ValidationException
    assertThrows(ValidationException.class, () -> {
        customFieldService.validateCustomFields("project", fields);
    });

    fields.put("client_name", "ACME Corp");
    // Should pass
    customFieldService.validateCustomFields("project", fields);
}
```

#### 2. Integration Tests
```java
@Test
public void testCustomFieldPersistence() {
    // Create custom field definition
    CustomFieldDefinition def = createDefinition("project", "client_name", "text");
    customFieldService.save(def);

    // Create project with custom field
    Project project = new Project();
    project.setName("Test Project");
    Map<String, Object> customFields = Map.of("client_name", "ACME");
    project.setCustomFields(gson.toJson(customFields));
    projectRepository.save(project);

    // Retrieve and verify
    Project retrieved = projectRepository.findById(project.getId());
    Map<String, Object> retrievedFields =
        customFieldService.parseCustomFields(retrieved.getCustomFields());
    assertEquals("ACME", retrievedFields.get("client_name"));
}
```

## Technical Implementation Details

### Maven Configuration

Both demos include:
- jOOQ code generation configured for custom field tables
- Liquibase for database migrations
- Enum converters for status fields
- SQLite JDBC driver
- Vaadin UI components

### jOOQ Code Generation

The `pom.xml` configures jOOQ to:
1. Run Liquibase migrations first
2. Generate type-safe table/field classes
3. Convert VARCHAR enums to Java enums
4. Output to `target/generated-sources`

### Liquibase Migrations

Three-stage migration structure:
1. `vortex-crud/V1.sql` - Framework tables (users, roles, audit)
2. `vortex-crud/V2.sql` - Version tracking
3. `database/V1.sql` - Application schema + sample data

## Next Steps

### For Project Management Demo
1. Implement `ProjectManagementConfiguration.java`
2. Configure routes for Projects, Tasks, Sprints
3. Implement Kanban board for tasks
4. Add time tracking UI
5. Build custom fields admin interface
6. Implement activity timeline

### For Development Platform Demo
1. Implement `DevPlatformConfiguration.java`
2. Configure routes for Repositories, Issues, PRs
3. Build file tree browser
4. Implement diff viewer for PRs
5. Add markdown rendering
6. Build custom fields admin interface
7. Implement activity feed

### Custom Fields Enhancement
1. Extend Vortex CRUD form system for JSON field binding
2. Create custom field admin UI
3. Implement field validation framework
4. Add field dependencies
5. Build field group support
6. Create calculated fields engine

## Conclusion

These demo applications showcase:

✅ **Complex data models** with 20-30+ tables
✅ **Rich relationships** (1:1, 1:N, N:M)
✅ **Custom fields architecture** with JSON storage
✅ **jOOQ integration** for type-safe queries
✅ **Comprehensive sample data** for realistic testing
✅ **Production-ready schemas** with indexes and constraints
✅ **Extensibility patterns** for custom requirements

The **custom fields system** is a powerful pattern that enables:
- Schema evolution without migrations
- Multi-tenant customization
- User-defined data models
- Dynamic UI generation
- Flexible reporting

This approach is used by leading SaaS platforms and represents a significant advancement over traditional fixed-schema CRUD applications.

## Resources

- **Vortex CRUD Documentation**: [Main Repository]
- **jOOQ Manual**: https://www.jooq.org/doc/latest/manual/
- **SQLite JSON Functions**: https://www.sqlite.org/json1.html
- **Vaadin Documentation**: https://vaadin.com/docs

## Author Notes

This implementation demonstrates **thoughtful consideration** of:
- Data modeling best practices
- Extensibility patterns
- Performance optimization
- Developer experience
- Production readiness

The demos are designed to be **learning resources** that showcase not just basic CRUD, but **advanced patterns** used in real-world applications.
