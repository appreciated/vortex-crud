-- Seed data for JOOQ kanban tests
DELETE FROM kanban_tasks;

INSERT INTO kanban_tasks (id, title, description, status, row_index) VALUES
  (1, 'Task A', 'First task', 'a', 0),
  (2, 'Task B', 'Second task', 'a', 1),
  (3, 'Task C', 'Third task', 'b', 0);
