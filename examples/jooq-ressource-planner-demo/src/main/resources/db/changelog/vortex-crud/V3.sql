-- liquibase formatted sql

-- changeset resource-planner-demo:5
CREATE TABLE email_templates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(200),
    body TEXT
);

CREATE TABLE settings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_agreement_text TEXT,
    default_email_template_id INTEGER,
    FOREIGN KEY (default_email_template_id) REFERENCES email_templates(id)
);

-- Insert default settings row
INSERT INTO settings (id, user_agreement_text) VALUES (1, 'By using this service, you agree to the terms and conditions.');

ALTER TABLE appointment ADD COLUMN user_agreement_accepted BOOLEAN DEFAULT FALSE;
