-- Seed data for JPA i18n smoke tests
DELETE FROM projects;
INSERT INTO projects (id, name, description, start_date, end_date, created_at, updated_at) VALUES
    (1, 'Project Alpha', 'A high-priority project aimed at improving the internal system', '2023-01-01 00:00:00', '2023-12-31 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
