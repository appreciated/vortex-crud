-- liquibase formatted sql

-- changeset resource-planner-demo:1
CREATE TABLE users
(
    id            INTEGER PRIMARY KEY,
    username      VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles
(
    id   INTEGER PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- changeset resource-planner-demo:2
CREATE TABLE room (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    capacity INTEGER DEFAULT 1,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE person (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    title VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE appointment_type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration_minutes INTEGER DEFAULT 60,
    price DECIMAL(10, 2),
    requires_room BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE person_appointment_type (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    person_id INTEGER NOT NULL,
    appointment_type_id INTEGER NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_type_id) REFERENCES appointment_type(id) ON DELETE CASCADE,
    UNIQUE(person_id, appointment_type_id)
);

CREATE TABLE appointment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    appointment_type_id INTEGER NOT NULL,
    room_id INTEGER,
    person_id INTEGER,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    status VARCHAR(50) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by_user_id INTEGER,
    FOREIGN KEY (appointment_type_id) REFERENCES appointment_type(id),
    FOREIGN KEY (room_id) REFERENCES room(id),
    FOREIGN KEY (person_id) REFERENCES person(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id)
);

-- changeset resource-planner-demo:3
CREATE INDEX idx_appointment_time ON appointment(start_time, end_time);
CREATE INDEX idx_appointment_room ON appointment(room_id);
CREATE INDEX idx_appointment_person ON appointment(person_id);
