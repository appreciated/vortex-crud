-- liquibase formatted sql

-- changeset jooq-sqlite-example-vortex-crud:13
ALTER TABLE projects ADD COLUMN budget DECIMAL(10, 2);
ALTER TABLE projects ADD COLUMN active BOOLEAN;

-- changeset jooq-sqlite-example-vortex-crud:14
CREATE TABLE documents (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    pdf VARCHAR(255)
);

-- changeset jooq-sqlite-example-vortex-crud:15
CREATE TABLE project_tags (
    project_id INTEGER NOT NULL,
    tag VARCHAR(255),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
