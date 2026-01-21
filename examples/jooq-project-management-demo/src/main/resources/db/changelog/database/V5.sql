-- liquibase formatted sql

-- changeset project-management-demo:15
-- Extended Seed Data for Project Management Demo
-- ============================================================================

-- Additional Labels
INSERT INTO label (name, color, description) VALUES
('feature', '#0e8a16', 'New feature development'),
('refactoring', '#fbca04', 'Code refactoring'),
('testing', '#1d76db', 'Testing related'),
('documentation', '#0075ca', 'Documentation updates'),
('infrastructure', '#5319e7', 'Infrastructure and DevOps'),
('performance', '#f9d0c4', 'Performance improvements'),
('ui/ux', '#d876e3', 'User interface improvements'),
('critical', '#b60205', 'Critical priority'),
('tech-debt', '#d93f0b', 'Technical debt');

-- Additional Projects
INSERT INTO project (name, code, description, status, priority, start_date, end_date, owner_id, progress_percentage, color, is_archived) VALUES
('E-Commerce Platform', 'ECOM', 'Full-stack e-commerce solution with payment integration', 'active', 'high', '2025-01-15', '2025-10-31', 1, 35, '#4CAF50', 0),
('Mobile Banking App', 'MBANK', 'Secure mobile banking application for iOS and Android', 'active', 'critical', '2025-02-01', '2025-09-30', 1, 20, '#2196F3', 0),
('Internal Dashboard', 'DASH', 'Analytics dashboard for internal reporting', 'planning', 'medium', '2025-03-01', '2025-08-15', 1, 5, '#FF9800', 0),
('Customer Portal', 'CUST', 'Self-service customer portal', 'active', 'high', '2025-01-20', '2025-07-31', 1, 45, '#9C27B0', 0),
('API Microservices', 'API', 'Microservices architecture for backend', 'active', 'high', '2024-11-01', '2025-06-30', 1, 60, '#F44336', 0),
('Legacy Migration', 'LEGACY', 'Migrate legacy system to modern stack', 'on_hold', 'medium', '2025-04-01', '2025-12-31', 1, 10, '#795548', 0);

-- Additional Project Members
INSERT INTO project_member (project_id, user_id, role) VALUES
-- Demo Project (1)
(1, 1, 'owner'),
-- E-Commerce Platform (2)
(2, 1, 'owner'),
-- Mobile Banking App (3)
(3, 1, 'owner'),
-- Internal Dashboard (4)
(4, 1, 'manager'),
-- Customer Portal (5)
(5, 1, 'owner'),
-- API Microservices (6)
(6, 1, 'owner'),
-- Legacy Migration (7)
(7, 1, 'manager');

-- Additional Milestones
INSERT INTO milestone (project_id, title, description, due_date, status, completion_percentage) VALUES
-- E-Commerce Platform milestones
(2, 'MVP Launch', 'Minimum viable product with core features', '2025-05-15', 'open', 40),
(2, 'Payment Integration', 'Integrate Stripe and PayPal payments', '2025-07-01', 'open', 20),
(2, 'Beta Testing', 'Public beta with selected users', '2025-09-15', 'open', 0),
-- Mobile Banking App milestones
(3, 'Authentication System', 'Secure authentication with biometrics', '2025-04-15', 'open', 30),
(3, 'Transaction Features', 'Transfer, payment, and transaction history', '2025-06-30', 'open', 10),
(3, 'Security Audit', 'Complete security audit and penetration testing', '2025-08-31', 'open', 0),
-- Customer Portal milestones
(5, 'User Registration', 'User registration and profile management', '2025-04-01', 'open', 70),
(5, 'Support Ticketing', 'Integrated support ticket system', '2025-06-15', 'open', 30),
-- API Microservices milestones
(6, 'Service Architecture', 'Complete microservices architecture design', '2025-02-28', 'completed', 100),
(6, 'Core Services', 'Implement core business services', '2025-04-30', 'open', 80),
(6, 'API Gateway', 'API gateway with rate limiting', '2025-06-15', 'open', 40);

-- Additional Sprints
INSERT INTO sprint (project_id, name, goal, start_date, end_date, status) VALUES
-- E-Commerce Platform sprints
(2, 'Sprint 1', 'Setup project and basic product catalog', '2025-01-15', '2025-01-29', 'completed'),
(2, 'Sprint 2', 'Shopping cart and checkout flow', '2025-01-30', '2025-02-13', 'completed'),
(2, 'Sprint 3', 'User authentication and profiles', '2025-02-14', '2025-02-28', 'active'),
(2, 'Sprint 4', 'Payment gateway integration', '2025-03-01', '2025-03-15', 'planned'),
-- Mobile Banking App sprints
(3, 'Sprint 1', 'Project setup and architecture', '2025-02-01', '2025-02-15', 'active'),
(3, 'Sprint 2', 'Authentication and security', '2025-02-16', '2025-03-02', 'planned'),
-- API Microservices sprints
(6, 'Sprint 5', 'User service completion', '2025-01-15', '2025-01-29', 'completed'),
(6, 'Sprint 6', 'Order service implementation', '2025-01-30', '2025-02-13', 'active'),
(6, 'Sprint 7', 'Inventory service', '2025-02-14', '2025-02-28', 'planned');

-- Additional Tasks
INSERT INTO task (project_id, milestone_id, sprint_id, parent_task_id, task_number, title, description, task_type, status, priority, assignee_id, reporter_id, estimated_hours, due_date) VALUES
-- E-Commerce Platform tasks
(2, 1, 1, NULL, 1, 'Design database schema', 'Create ERD and database schema for products, orders, and users', 'task', 'done', 'high', 1, 1, 16, '2025-01-20'),
(2, 1, 1, NULL, 2, 'Setup development environment', 'Configure local development environment with Docker', 'task', 'done', 'medium', 1, 1, 8, '2025-01-18'),
(2, 1, 2, NULL, 3, 'Implement product catalog', 'Create product listing with search and filters', 'story', 'done', 'high', 1, 1, 40, '2025-02-10'),
(2, 1, 2, NULL, 4, 'Create shopping cart', 'Shopping cart with add/remove/update quantities', 'story', 'done', 'high', 1, 1, 32, '2025-02-13'),
(2, 1, 3, NULL, 5, 'User registration system', 'Email-based user registration with verification', 'story', 'in_progress', 'high', 1, 1, 24, '2025-02-25'),
(2, 1, 3, 5, 6, 'Email verification API', 'REST API for email verification', 'task', 'in_progress', 'high', 1, 1, 8, '2025-02-22'),
(2, 1, 3, 5, 7, 'User profile page', 'Profile page with edit functionality', 'task', 'todo', 'medium', NULL, 1, 16, '2025-02-26'),
(2, 2, 4, NULL, 8, 'Stripe payment integration', 'Integrate Stripe for credit card payments', 'story', 'todo', 'critical', NULL, 1, 40, '2025-03-12'),
(2, 2, 4, NULL, 9, 'PayPal payment integration', 'Add PayPal as payment option', 'story', 'todo', 'high', NULL, 1, 32, '2025-03-14'),
(2, NULL, 3, NULL, 10, 'Fix product image upload bug', 'Product images not uploading correctly', 'bug', 'in_progress', 'high', 1, 1, 4, '2025-02-20'),
(2, NULL, 3, NULL, 11, 'Optimize database queries', 'Product search is slow with large catalog', 'task', 'todo', 'medium', NULL, 1, 8, '2025-02-27'),

-- Mobile Banking App tasks
(3, 2, 5, NULL, 1, 'Architecture design', 'Design app architecture and tech stack', 'task', 'done', 'high', 1, 1, 24, '2025-02-08'),
(3, 2, 5, NULL, 2, 'Setup CI/CD pipeline', 'Configure automated build and deployment', 'task', 'in_progress', 'medium', 1, 1, 16, '2025-02-14'),
(3, 3, 5, NULL, 3, 'Implement biometric authentication', 'Face ID and Touch ID support', 'story', 'in_progress', 'critical', 1, 1, 40, '2025-02-28'),
(3, 3, 6, NULL, 4, 'Two-factor authentication', 'SMS and authenticator app 2FA', 'story', 'todo', 'critical', NULL, 1, 32, '2025-03-15'),
(3, 4, NULL, NULL, 5, 'Transaction history screen', 'Display user transaction history', 'story', 'todo', 'high', NULL, 1, 24, '2025-04-15'),
(3, 4, NULL, NULL, 6, 'Fund transfer feature', 'Transfer money between accounts', 'story', 'todo', 'critical', NULL, 1, 40, '2025-05-01'),
(3, NULL, 5, NULL, 7, 'Fix crash on Android 12', 'App crashes on Android 12 devices', 'bug', 'in_progress', 'critical', 1, 1, 8, '2025-02-16'),

-- Internal Dashboard tasks
(4, NULL, NULL, NULL, 1, 'Requirements gathering', 'Gather requirements from stakeholders', 'task', 'in_progress', 'high', 1, 1, 16, '2025-03-10'),
(4, NULL, NULL, NULL, 2, 'Design mockups', 'Create UI mockups for dashboard', 'task', 'todo', 'medium', NULL, 1, 24, '2025-03-20'),
(4, NULL, NULL, NULL, 3, 'Data source integration', 'Connect to analytics data sources', 'task', 'todo', 'high', NULL, 1, 32, '2025-04-05'),

-- Customer Portal tasks
(5, 6, NULL, NULL, 1, 'User registration', 'Customer self-registration with email verification', 'story', 'done', 'high', 1, 1, 32, '2025-03-25'),
(5, 6, NULL, NULL, 2, 'Login and password reset', 'Secure login with password reset flow', 'story', 'done', 'high', 1, 1, 24, '2025-03-28'),
(5, 6, NULL, NULL, 3, 'User profile management', 'View and edit user profile information', 'story', 'in_progress', 'medium', 1, 1, 16, '2025-04-10'),
(5, 7, NULL, NULL, 4, 'Create support ticket', 'Allow users to create support tickets', 'story', 'in_progress', 'high', 1, 1, 32, '2025-05-15'),
(5, 7, NULL, NULL, 5, 'View ticket history', 'Display user ticket history and status', 'story', 'todo', 'medium', NULL, 1, 24, '2025-05-30'),
(5, NULL, NULL, NULL, 6, 'FAQ section', 'Searchable FAQ for common questions', 'story', 'todo', 'low', NULL, 1, 20, '2025-06-15'),

-- API Microservices tasks
(6, 8, 7, NULL, 1, 'Service discovery setup', 'Configure Consul for service discovery', 'task', 'done', 'high', 1, 1, 16, '2025-01-25'),
(6, 9, 7, NULL, 2, 'User service API', 'Complete user management microservice', 'story', 'done', 'high', 1, 1, 40, '2025-02-10'),
(6, 9, 8, NULL, 3, 'Order service implementation', 'Implement order processing service', 'story', 'in_progress', 'high', 1, 1, 48, '2025-02-28'),
(6, 9, 8, 3, 4, 'Order creation API', 'REST API for creating orders', 'task', 'in_progress', 'high', 1, 1, 16, '2025-02-20'),
(6, 9, 8, 3, 5, 'Order status tracking', 'Track order status changes', 'task', 'todo', 'high', NULL, 1, 16, '2025-02-25'),
(6, 10, 9, NULL, 6, 'Inventory service', 'Manage product inventory', 'story', 'todo', 'high', NULL, 1, 40, '2025-03-15'),
(6, 10, NULL, NULL, 7, 'API gateway rate limiting', 'Implement rate limiting in gateway', 'task', 'todo', 'medium', NULL, 1, 24, '2025-04-01'),
(6, NULL, 8, NULL, 8, 'Fix service communication timeout', 'Inter-service calls timing out', 'bug', 'in_progress', 'critical', 1, 1, 8, '2025-02-18'),

-- Legacy Migration tasks
(7, NULL, NULL, NULL, 1, 'Current system analysis', 'Analyze legacy system architecture', 'task', 'done', 'high', 1, 1, 40, '2025-04-15'),
(7, NULL, NULL, NULL, 2, 'Data migration strategy', 'Design data migration approach', 'task', 'todo', 'high', NULL, 1, 32, '2025-05-01'),
(7, NULL, NULL, NULL, 3, 'API compatibility layer', 'Build compatibility layer for legacy APIs', 'story', 'todo', 'medium', NULL, 1, 48, '2025-06-01');

-- Link tasks to labels
INSERT INTO task_label (task_id, label_id) VALUES
-- E-Commerce Platform
(3, 1), -- Setup project - bug
(4, 1), -- Implement feature - bug
(5, 3), -- Design database - feature
(6, 3), -- Setup dev environment - feature
(7, 3), -- Product catalog - feature
(8, 3), -- Shopping cart - feature
(9, 3), -- User registration - feature
(12, 3), -- Stripe integration - feature
(13, 3), -- PayPal integration - feature
(14, 1), -- Fix upload bug - bug
(15, 8), -- Optimize queries - critical
-- Mobile Banking App
(16, 3), -- Architecture design - feature
(17, 7), -- CI/CD - infrastructure
(18, 3), -- Biometric auth - feature
(19, 3), -- 2FA - feature
(23, 1), -- Fix crash - bug
(23, 8), -- Fix crash - critical
-- Customer Portal
(27, 3), -- User registration - feature
(28, 3), -- Login - feature
(30, 3), -- Support ticket - feature
-- API Microservices
(34, 7), -- Service discovery - infrastructure
(35, 3), -- User service - feature
(36, 3), -- Order service - feature
(41, 1), -- Fix timeout - bug
(41, 8); -- Fix timeout - critical

-- Additional Task Comments
INSERT INTO task_comment (task_id, author_id, content) VALUES
(5, 1, 'Started working on the database schema. Will share the ERD for review.'),
(7, 1, 'Product catalog is looking good. Need to add pagination for large datasets.'),
(8, 1, 'Shopping cart functionality is complete. Testing edge cases now.'),
(9, 1, 'Email verification is implemented. Awaiting code review.'),
(14, 1, 'Found the issue. It was a MIME type configuration problem.'),
(14, 1, 'Fix deployed and tested. Closing this issue.'),
(18, 1, 'Biometric authentication working on iOS. Testing Android implementation.'),
(23, 1, 'Reproduced the crash. It is related to the permissions system.'),
(23, 1, 'Fix implemented. Need to test on multiple Android 12 devices.'),
(30, 1, 'Support ticket creation is working. Adding file attachment support next.'),
(36, 1, 'Order service architecture is complete. Starting API implementation.'),
(41, 1, 'Increased timeout values. Monitoring performance.');

-- Additional Time Entries
INSERT INTO time_entry (task_id, user_id, hours_spent, description, entry_date) VALUES
-- E-Commerce tasks
(5, 1, 8.0, 'Database schema design and documentation', '2025-01-16'),
(5, 1, 6.5, 'Schema refinement based on feedback', '2025-01-19'),
(6, 1, 4.0, 'Docker compose setup', '2025-01-17'),
(7, 1, 12.0, 'Product catalog frontend implementation', '2025-02-05'),
(7, 1, 10.0, 'Search and filter functionality', '2025-02-07'),
(8, 1, 8.0, 'Shopping cart state management', '2025-02-10'),
(8, 1, 7.5, 'Cart persistence and API integration', '2025-02-12'),
(9, 1, 6.0, 'User registration API', '2025-02-18'),
(9, 1, 5.5, 'Email verification implementation', '2025-02-20'),
(10, 1, 4.0, 'Email verification API endpoint', '2025-02-21'),
(14, 1, 2.5, 'Bug investigation and fix', '2025-02-19'),
-- Mobile Banking tasks
(16, 1, 10.0, 'Architecture research and design', '2025-02-03'),
(16, 1, 8.0, 'Tech stack evaluation and documentation', '2025-02-06'),
(17, 1, 6.0, 'CI/CD pipeline setup', '2025-02-10'),
(18, 1, 8.0, 'iOS biometric authentication', '2025-02-12'),
(18, 1, 7.0, 'Android biometric implementation', '2025-02-14'),
(23, 1, 3.0, 'Bug reproduction and analysis', '2025-02-15'),
(23, 1, 4.5, 'Fix implementation and testing', '2025-02-16'),
-- Customer Portal tasks
(27, 1, 10.0, 'Registration flow implementation', '2025-03-18'),
(28, 1, 8.0, 'Login and authentication', '2025-03-22'),
(30, 1, 6.0, 'Support ticket API', '2025-04-10'),
-- API Microservices tasks
(34, 1, 8.0, 'Consul setup and configuration', '2025-01-20'),
(35, 1, 16.0, 'User service implementation', '2025-02-05'),
(36, 1, 12.0, 'Order service initial development', '2025-02-15'),
(37, 1, 8.0, 'Order creation API', '2025-02-17'),
(41, 1, 4.0, 'Timeout issue debugging and fix', '2025-02-18');

-- Additional Notifications
INSERT INTO notification (user_id, title, message, link, is_read) VALUES
(1, 'Task assigned', 'You have been assigned to task "Fix product image upload bug"', '/projects/2/tasks/10', 0),
(1, 'Task due soon', 'Task "Email verification API" is due in 2 days', '/projects/2/tasks/6', 0),
(1, 'Sprint started', 'Sprint 3 has started', '/projects/2/sprints/3', 1),
(1, 'Comment on task', 'New comment on task "Shopping cart"', '/projects/2/tasks/4', 1),
(1, 'Milestone completed', 'Milestone "Service Architecture" has been completed', '/projects/6/milestones/9', 1),
(1, 'Task overdue', 'Task "Fix crash on Android 12" is overdue', '/projects/3/tasks/7', 0),
(1, 'New task created', 'Task "Data migration strategy" has been created', '/projects/7/tasks/2', 1),
(1, 'Time entry reminder', 'Remember to log your time for today', '/time-entries', 0);

-- Sample Attachments (file paths are examples)
INSERT INTO attachment (task_id, uploader_id, name, media_type, size, path) VALUES
(5, 1, 'database-schema.png', 'image/png', 245678, '/uploads/attachments/database-schema.png'),
(5, 1, 'erd-diagram.pdf', 'application/pdf', 512340, '/uploads/attachments/erd-diagram.pdf'),
(16, 1, 'architecture-diagram.png', 'image/png', 389123, '/uploads/attachments/architecture-diagram.png'),
(16, 1, 'tech-stack-analysis.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 78456, '/uploads/attachments/tech-stack-analysis.docx'),
(23, 1, 'crash-log.txt', 'text/plain', 12345, '/uploads/attachments/crash-log.txt'),
(30, 1, 'ticket-flow-mockup.png', 'image/png', 456789, '/uploads/attachments/ticket-flow-mockup.png');
