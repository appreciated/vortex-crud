-- Seed data for JOOQ kanban tests
DELETE FROM tasks;

INSERT INTO tasks (id, title, description, status) VALUES
  (1, 'Task A', 'First task', 'ToDo'),
  (2, 'Task B', 'Second task', 'In progress');
