-- Tabelle für Benutzer
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    role          VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabelle für Rollen (optional, wenn Rollen dynamisch verwaltet werden sollen)
CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabelle für Sammlungen (Collections)
CREATE TABLE collections
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL UNIQUE,
    schema_definition JSON         NOT NULL, -- JSON-Schema der Collection
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabelle für View-Konfigurationen pro Collection
CREATE TABLE view_configs
(
    id            SERIAL PRIMARY KEY,
    collection_id INT REFERENCES collections (id) ON DELETE CASCADE,
    view_name     VARCHAR(255) NOT NULL, -- Name der Ansicht, z.B. "Detailansicht", "Listenansicht"
    config        JSON         NOT NULL, -- JSON-Schema der Ansicht
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabelle für Audit-Logs
CREATE TABLE audit_log
(
    id                SERIAL PRIMARY KEY,
    user_id           INT         NOT NULL REFERENCES users (id) ON DELETE CASCADE, -- Der Benutzer, der die Aktion durchgeführt hat
    action            VARCHAR(50) NOT NULL,                                         -- Art der Aktion, z.B. "create", "update", "delete", "login", "logout"
    target_collection VARCHAR(255),                                                 -- Die Sammlung, auf die die Aktion angewendet wurde
    target_record_id  INT,                                                          -- ID des Datensatzes, auf den die Aktion angewendet wurde
    description       TEXT,                                                         -- Beschreibung der Aktion
    timestamp         TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           -- Zeitpunkt der Aktion
);
