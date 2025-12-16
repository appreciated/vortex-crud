-- liquibase formatted sql

-- changeset jules:seed-users-additional
INSERT INTO users (username, password_hash, first_name, last_name, created_at)
VALUES ('admin@example.com', '$2a$10$XJkJkVGCZGkgh0IRoM.bmeBqCORrO6NQkyRIsykA5wb/hP5bLfUKy', 'Admin', 'User', CURRENT_TIMESTAMP);

INSERT INTO users (username, password_hash, first_name, last_name, created_at)
VALUES ('user@example.com', '$2a$10$XJkJkVGCZGkgh0IRoM.bmeBqCORrO6NQkyRIsykA5wb/hP5bLfUKy', 'Normal', 'User', CURRENT_TIMESTAMP);

-- changeset jules:seed-user-roles-additional
INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE username = 'admin@example.com'),
    (SELECT id FROM roles WHERE name = 'admin')
);

INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE username = 'user@example.com'),
    (SELECT id FROM roles WHERE name = 'viewer')
);
