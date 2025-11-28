-- liquibase formatted sql

-- changeset jooq-sqlite-example-test-project:13
ALTER TABLE projects ADD COLUMN tags VARCHAR;
ALTER TABLE projects ADD COLUMN active BOOLEAN;
ALTER TABLE projects ADD COLUMN budget DECIMAL;
ALTER TABLE projects ADD COLUMN notes TEXT;

-- changeset jooq-sqlite-example-test-project:14
CREATE TABLE documents
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR,
    url   VARCHAR
);
