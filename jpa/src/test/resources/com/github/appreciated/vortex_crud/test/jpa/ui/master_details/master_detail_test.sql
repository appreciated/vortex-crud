-- Seed data for JPA master-detail tests
DELETE FROM task_has_task;
DELETE FROM task_comments;
DELETE FROM tasks;

INSERT INTO tasks (id, title, description, status) VALUES
  (1, 'Done Task A', 'Finished task', 'Done'),
  (2, 'Done Task B', 'Another finished task', 'Done');
