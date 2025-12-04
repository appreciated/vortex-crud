-- liquibase formatted sql

-- changeset jooq-sqlite-example-test-project:13
ALTER TABLE projects ADD COLUMN pdf_url VARCHAR(255);
ALTER TABLE projects ADD COLUMN date_time_range VARCHAR(2000);
