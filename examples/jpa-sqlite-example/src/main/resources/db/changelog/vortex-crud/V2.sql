-- liquibase formatted sql

-- changeset jpa-sqlite-example-vortex-crud:5
-- Insert roles
INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO roles (id, name) VALUES (2, 'manager');
INSERT INTO roles (id, name) VALUES (3, 'editor');
INSERT INTO roles (id, name) VALUES (4, 'viewer');
INSERT INTO roles (id, name) VALUES (5, 'guest');

-- changeset jpa-sqlite-example-vortex-crud:6
-- Update existing users with password hashes and names, or insert if they don't exist
-- Password for all users is 'password'

-- Update existing users from database/V1.sql with proper auth data
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'Max', last_name = 'Mustermann' WHERE id = 1;
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'Erika', last_name = 'Musterfrau' WHERE id = 2;
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'John', last_name = 'Doe' WHERE id = 3;
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'Jane', last_name = 'Doe' WHERE id = 4;

-- Insert new test users with specific roles
INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (5, 'admin@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Admin', 'User');

INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (6, 'manager@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Manager', 'User');

INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (7, 'editor@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Editor', 'User');

INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (8, 'viewer@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Viewer', 'User');

INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (9, 'guest@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Guest', 'User');

-- Additional test user with multiple roles
INSERT INTO users (id, username, password_hash, first_name, last_name)
VALUES (10, 'manager.editor@example.com', '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Manager', 'Editor');

-- changeset jpa-sqlite-example-vortex-crud:7
-- Assign roles to users
-- Existing users from database/V1.sql (IDs 1-4) get basic viewer/editor access
INSERT INTO user_roles (user_id, role_id) VALUES (1, 4); -- viewer role for max@mustermann.de
INSERT INTO user_roles (user_id, role_id) VALUES (2, 4); -- viewer role for erika@musterfrau.de
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- editor role for john@doe.com
INSERT INTO user_roles (user_id, role_id) VALUES (4, 3); -- editor role for jane@doe.com

-- New test users with specific role-based access (IDs 5-10)
INSERT INTO user_roles (user_id, role_id) VALUES (5, 1); -- admin role for admin@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (6, 2); -- manager role for manager@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (7, 3); -- editor role for editor@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (8, 4); -- viewer role for viewer@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (9, 5); -- guest role for guest@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (10, 2); -- manager role for manager.editor@example.com
INSERT INTO user_roles (user_id, role_id) VALUES (10, 3); -- editor role for manager.editor@example.com