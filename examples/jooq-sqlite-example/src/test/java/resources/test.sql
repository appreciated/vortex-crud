-- Beispiel-Rolle einfügen
INSERT INTO roles (name) VALUES ('admin'), ('editor'), ('viewer');

-- Beispiel-Benutzer einfügen
INSERT INTO users (username, password_hash, email, role)
VALUES ('admin_user', 'hashed_password', 'admin@example.com', 'admin');
