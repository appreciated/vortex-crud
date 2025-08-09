-- Basic configuration test for JPA
CREATE TABLE IF NOT EXISTS jpa_config_test (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    value VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE
);

INSERT INTO jpa_config_test (id, name, value, enabled)
VALUES 
    (1, 'max_connections', '100', TRUE),
    (2, 'timeout_seconds', '30', TRUE),
    (3, 'debug_mode', 'false', FALSE);