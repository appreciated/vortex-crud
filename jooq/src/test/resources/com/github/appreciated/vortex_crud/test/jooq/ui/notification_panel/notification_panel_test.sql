DROP TABLE IF EXISTS notification_panel_test;
CREATE TABLE notification_panel_test (
    id INTEGER PRIMARY KEY,
    message VARCHAR(255),
    timestamp DATETIME,
    read BOOLEAN
);

INSERT INTO notification_panel_test (id, message, timestamp, read) VALUES (1, 'New task assigned', CURRENT_TIMESTAMP, 0);
INSERT INTO notification_panel_test (id, message, timestamp, read) VALUES (2, 'Welcome', CURRENT_TIMESTAMP, 1);
