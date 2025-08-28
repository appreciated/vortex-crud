-- Seed data for JOOQ master-detail tests
DELETE FROM master_detail_tasks;

INSERT INTO master_detail_tasks (id, title, description, status) VALUES
  (1, 'Done Task A', 'Finished task', 'Done'),
  (2, 'Done Task B', 'Another finished task', 'Done');
