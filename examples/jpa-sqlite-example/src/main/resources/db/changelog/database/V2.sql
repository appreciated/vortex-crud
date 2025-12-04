-- liquibase formatted sql

-- changeset jpa-sqlite-example-vortex-crud:13
ALTER TABLE projects ADD COLUMN pdf_url VARCHAR(255);
ALTER TABLE projects ADD COLUMN start_date_time TIMESTAMP;
ALTER TABLE projects ADD COLUMN end_date_time TIMESTAMP;
