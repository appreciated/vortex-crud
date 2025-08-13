-- Seed data for JOOQ tasks tests
DELETE FROM task_has_task;
DELETE FROM task_comments;
DELETE FROM tasks;

INSERT INTO tasks (id, title, description, status) VALUES
  (1, 'Task A', 'First task', 'ToDo'),
  (2, 'Task B', 'Second task', 'In progress');
