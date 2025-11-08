-- liquibase formatted sql

-- changeset jooq-sqlite-example-vortex-crud:5
-- Insert roles
INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO roles (id, name) VALUES (2, 'viewer');
INSERT INTO roles (id, name) VALUES (3, 'guest');

-- changeset jooq-sqlite-example-vortex-crud:6
-- Update existing users with password hashes and names, or insert if they don't exist
-- Password for all users is 'password'

-- Update existing users from database/V1.sql with proper auth data
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'Max', last_name = 'Mustermann' WHERE id = 1;
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'Erika', last_name = 'Musterfrau' WHERE id = 2;
UPDATE users SET password_hash = '$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', first_name = 'John', last_name = 'Doe' WHERE id = 3;

-- changeset jooq-sqlite-example-vortex-crud:7
-- Assign roles to users
-- Existing users from database/V1.sql
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- viewer role for max@mustermann.de
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- viewer role for erika@musterfrau.de
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- editor role for john@doe.com