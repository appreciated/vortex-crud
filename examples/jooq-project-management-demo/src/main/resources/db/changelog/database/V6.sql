-- liquibase formatted sql

-- changeset project-management-demo:16
-- Add project_id to custom_field_definition to make custom fields project-specific
-- ============================================================================

-- Add project_id column to custom_field_definition
ALTER TABLE custom_field_definition ADD COLUMN project_id INTEGER;

-- Add foreign key constraint
-- Note: SQLite doesn't support adding foreign keys to existing tables
-- So we need to recreate the table with the foreign key

-- Create new table with foreign key
CREATE TABLE custom_field_definition_new (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER,
    entity_type VARCHAR(50) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    field_label VARCHAR(100) NOT NULL,
    field_type VARCHAR(50) NOT NULL,
    field_order INTEGER DEFAULT 0,
    is_required INTEGER DEFAULT 0,
    default_value TEXT,
    options TEXT,
    validation_rules TEXT,
    description TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    is_active INTEGER DEFAULT 1,
    UNIQUE(project_id, entity_type, field_name),
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- Copy existing data (set project_id to NULL for global fields)
INSERT INTO custom_field_definition_new
    (id, project_id, entity_type, field_name, field_label, field_type, field_order,
     is_required, default_value, options, validation_rules, description, created_at, created_by, is_active)
SELECT
    id, NULL, entity_type, field_name, field_label, field_type, field_order,
    is_required, default_value, options, validation_rules, description, created_at, created_by, is_active
FROM custom_field_definition;

-- Drop old table
DROP TABLE custom_field_definition;

-- Rename new table
ALTER TABLE custom_field_definition_new RENAME TO custom_field_definition;

-- Recreate index
CREATE INDEX idx_custom_field_entity ON custom_field_definition(entity_type);
CREATE INDEX idx_custom_field_project ON custom_field_definition(project_id);

-- changeset project-management-demo:17
-- Sample project-specific custom field definitions
-- ============================================================================

-- Add custom fields for specific projects
INSERT INTO custom_field_definition (project_id, entity_type, field_name, field_label, field_type, is_required, description) VALUES
-- E-Commerce Platform custom fields
(2, 'project', 'budget', 'Project Budget', 'number', 1, 'Total project budget in USD'),
(2, 'project', 'client_contact', 'Client Contact Email', 'text', 0, 'Primary contact email for the client'),
(2, 'task', 'estimated_cost', 'Estimated Cost', 'number', 0, 'Estimated cost for this task'),

-- Mobile Banking App custom fields
(3, 'project', 'compliance_status', 'Compliance Status', 'select', 1, 'Banking compliance status'),
(3, 'project', 'security_level', 'Security Level', 'select', 1, 'Required security level'),
(3, 'task', 'security_review_required', 'Security Review Required', 'checkbox', 0, 'Whether this task requires security review'),

-- API Microservices custom fields
(6, 'project', 'deployment_environment', 'Deployment Environment', 'select', 0, 'Target deployment environment'),
(6, 'task', 'api_version', 'API Version', 'text', 0, 'API version this task affects');
