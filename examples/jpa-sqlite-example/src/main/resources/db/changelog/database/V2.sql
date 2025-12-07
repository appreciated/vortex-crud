-- liquibase formatted sql

-- changeset jpa-sqlite-example-vortex-crud:13
ALTER TABLE projects ADD COLUMN budget NUMERIC(38, 2);
ALTER TABLE projects ADD COLUMN active BOOLEAN;

-- changeset jpa-sqlite-example-vortex-crud:14
CREATE TABLE documents (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    pdf VARCHAR(255)
);

-- changeset jpa-sqlite-example-vortex-crud:15
CREATE TABLE project_tags (
    project_id INTEGER NOT NULL,
    tag VARCHAR(255),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);
