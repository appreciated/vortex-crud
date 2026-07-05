-- liquibase formatted sql

-- changeset jooq-sqlite-example-vortex-crud:5
-- Insert roles
INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO roles (id, name) VALUES (2, 'viewer');
INSERT INTO roles (id, name) VALUES (3, 'guest');

-- changeset jooq-sqlite-example-vortex-crud:6
-- Add first_name and last_name columns to users table
ALTER TABLE users ADD COLUMN first_name VARCHAR(255);
ALTER TABLE users ADD COLUMN last_name VARCHAR(255);

-- changeset jooq-sqlite-example-vortex-crud:8
-- Update existing users with password hashes and names, or insert if they don't exist
-- Password for all users is 'password'

-- Update existing users from database/V1.sql with proper auth data
INSERT INTO users (id, username, password_hash, first_name, last_name) VALUES (1, 'max@mustermann.de','$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Max', 'Mustermann');
INSERT INTO users (id, username, password_hash, first_name, last_name) VALUES (2, 'erika@musterfrau.de','$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'Erika', 'Musterfrau');
INSERT INTO users (id, username, password_hash, first_name, last_name) VALUES (3, 'john@doe.com','$2a$12$7WbHd.u40WxYhw2Uty.qzepjLLfyzm4f7Ns6jgwLUjNxRsWyuwxXG', 'John', 'Doe');

-- changeset jooq-sqlite-example-vortex-crud:7
-- Assign roles to users
-- Existing users from database/V1.sql
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- viewer admin for max@mustermann.de
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- viewer role for erika@musterfrau.de
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- editor guest for john@doe.com
-- changeset project-management-demo-vortex-crud:1
-- Add manager and developer roles referenced by availableRoles/defaultWriteRoles
INSERT INTO roles (id, name) VALUES (4, 'manager');
INSERT INTO roles (id, name) VALUES (5, 'developer');
INSERT INTO user_roles (user_id, role_id) VALUES (2, 4); -- manager role for erika@musterfrau.de
INSERT INTO user_roles (user_id, role_id) VALUES (3, 5); -- developer role for john@doe.com
