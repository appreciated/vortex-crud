-- liquibase formatted sql

-- changeset jooq-sqlite-example-vortex-crud:13
ALTER TABLE projects ADD COLUMN budget NUMERIC(38, 2);
ALTER TABLE projects ADD COLUMN active BOOLEAN;
ALTER TABLE projects ADD COLUMN tags_multi VARCHAR(1000);

-- changeset jooq-sqlite-example-vortex-crud:14
CREATE TABLE documents (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    pdf VARCHAR(255)
);

-- changeset jooq-sqlite-example-vortex-crud:15
CREATE TABLE project_tags (
    id INTEGER PRIMARY KEY,
    project_id INTEGER NOT NULL,
    tag VARCHAR(255),
    FOREIGN KEY (project_id) REFERENCES projects(id)
);

-- changeset jooq-sqlite-example-vortex-crud:16
-- Seed data for documents, project tags and the newer project columns
INSERT INTO documents (id, title, pdf) VALUES
(1, 'Project Charter', NULL),
(2, 'Requirements Specification', NULL),
(3, 'Architecture Overview', NULL);

INSERT INTO project_tags (project_id, tag) VALUES
(1, 'internal'),
(1, 'high-priority'),
(2, 'web'),
(2, 'customer-facing'),
(3, 'research'),
(3, 'ai');

UPDATE projects SET budget = 250000.00, active = 1, tags_multi = 'tag1,tag2' WHERE id = 1;
UPDATE projects SET budget = 480000.00, active = 1, tags_multi = 'tag2' WHERE id = 2;
UPDATE projects SET budget = 120000.00, active = 0, tags_multi = 'tag1,tag3' WHERE id = 3;
UPDATE projects SET budget = 75000.00, active = 1 WHERE id = 4;
