-- liquibase formatted sql

-- changeset jpa-sqlite-example-vortex-crud:13
ALTER TABLE projects ADD COLUMN tags VARCHAR;
ALTER TABLE projects ADD COLUMN active BOOLEAN;
ALTER TABLE projects ADD COLUMN budget DECIMAL;
ALTER TABLE projects ADD COLUMN notes TEXT;

-- changeset jpa-sqlite-example-vortex-crud:14
CREATE TABLE documents
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR,
    url   VARCHAR
);
