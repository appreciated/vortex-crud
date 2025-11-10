-- Clean up existing data
DELETE FROM multi_form_test;

-- Create test data
INSERT INTO multi_form_test (id, profile_name, email, description, age)
VALUES (1, 'Profile Name', 'profile@example.com', 'This is a profile description', 25);
