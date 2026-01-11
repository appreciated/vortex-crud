-- liquibase formatted sql

-- changeset resource-planner-demo:5
ALTER TABLE room ADD COLUMN working_hours_start VARCHAR(5);
ALTER TABLE room ADD COLUMN working_hours_end VARCHAR(5);

ALTER TABLE person ADD COLUMN working_hours_start VARCHAR(5);
ALTER TABLE person ADD COLUMN working_hours_end VARCHAR(5);
