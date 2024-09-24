

-- Beispiel-Daten einfügen (optional)

-- Beispiel-Rolle einfügen
INSERT INTO roles (name) VALUES ('admin'), ('editor'), ('viewer');

-- Beispiel-Benutzer einfügen
INSERT INTO users (username, password_hash, email, role)
VALUES ('admin_user', 'hashed_password', 'admin@example.com', 'admin');

-- Beispiel-Collection einfügen
INSERT INTO collections (name, schema_definition)
VALUES ('Articles', '{"fields": [{"name": "title", "type": "text"}, {"name": "content", "type": "textarea"}]}');

-- Beispiel-Felder für Collection "Articles" einfügen
INSERT INTO fields (collection_id, field_name, field_type, required, position)
VALUES (1, 'title', 'text', TRUE, 1),
       (1, 'content', 'textarea', TRUE, 2);

-- Beispiel-View-Konfiguration für Collection "Articles" einfügen
INSERT INTO view_configs (collection_id, view_name, config)
VALUES (1, 'Detailansicht', '{"fields": [{"field": "title", "type": "text", "label": "Titel"}, {"field": "content", "type": "textarea", "label": "Inhalt"}], "layout": "form"}');

-- Beispiel-Datenpunkt (Record) für Collection "Articles" einfügen
INSERT INTO records (collection_id, data)
VALUES (1, '{"title": "Beispielartikel", "content": "Dies ist ein Beispielinhalt."}');
