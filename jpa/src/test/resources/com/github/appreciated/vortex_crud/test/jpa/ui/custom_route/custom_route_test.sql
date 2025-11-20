-- Test data for CustomRoute tests
-- Creates sample entities to test alongside custom route

-- Clean up any existing data
DELETE FROM jpa_custom_route_test_entity;

-- Create test entities
INSERT INTO jpa_custom_route_test_entity (id, name, description) VALUES
    (1, 'Test Item 1', 'This is test item 1'),
    (2, 'Test Item 2', 'This is test item 2'),
    (3, 'Test Item 3', 'This is test item 3');
