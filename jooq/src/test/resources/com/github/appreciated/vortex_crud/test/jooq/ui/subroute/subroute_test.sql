-- Seed data for JOOQ subroute tests
DELETE FROM task_has_task;
DELETE FROM task_comments;
DELETE FROM tasks;

INSERT INTO tasks (id, title, description, status) VALUES
  (1, 'Subroute Task A', 'First task', 'Open'),
  (2, 'Subroute Task B', 'Second task', 'Done');
