DELETE FROM calendar_events;
-- Insert events with specific dates for today and tomorrow to ensure visibility
INSERT INTO calendar_events (title, start_date, end_date) VALUES ('Event A', datetime('now'), datetime('now', '+2 hours'));
INSERT INTO calendar_events (title, start_date, end_date) VALUES ('Event B', datetime('now', '+1 day'), datetime('now', '+1 day', '+2 hours'));
