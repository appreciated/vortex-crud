-- Seed data for JOOQ subroute tests
DELETE FROM subroute_tasks;

INSERT INTO subroute_tasks (id, title, description, status) VALUES
  (1, 'Subroute Task A', 'First task', 'Open'),
  (2, 'Subroute Task B', 'Second task', 'Done');
