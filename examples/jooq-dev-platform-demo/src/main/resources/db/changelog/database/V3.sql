-- liquibase formatted sql

-- changeset dev-platform-demo:21
-- Extended Seed Data for Dev Platform Demo
-- ============================================================================

-- Additional Organizations
INSERT INTO organization (name, display_name, description, website, is_active) VALUES
('tech-innovators', 'Tech Innovators Inc', 'Building the future of technology', 'https://techinnovators.example.com', 1),
('open-source-collective', 'Open Source Collective', 'Community-driven open source projects', 'https://osc.example.com', 1),
('startup-labs', 'Startup Labs', 'Experimental startup projects', 'https://startuplabs.example.com', 1),
('enterprise-solutions', 'Enterprise Solutions Co', 'Enterprise software development', 'https://enterprisesolutions.example.com', 1);

-- Additional Repositories
INSERT INTO repository (name, slug, description, owner_id, organization_id, visibility, default_branch, language, topics, readme_content, star_count, fork_count) VALUES
('web-framework', 'web-framework', 'Modern web framework for rapid development', 1, 2, 'public', 'main', 'JavaScript', 'web,framework,javascript', '# Web Framework\n\nA modern, fast web framework.', 245, 32),
('mobile-app', 'mobile-app', 'Cross-platform mobile application', 1, 2, 'public', 'main', 'Kotlin', 'mobile,android,ios', '# Mobile App\n\nCross-platform mobile solution.', 189, 21),
('api-gateway', 'api-gateway', 'High-performance API gateway', 1, 3, 'public', 'main', 'Go', 'api,gateway,microservices', '# API Gateway\n\nScalable API gateway service.', 567, 89),
('data-pipeline', 'data-pipeline', 'ETL data pipeline for big data', 1, 3, 'private', 'main', 'Python', 'data,etl,bigdata', '# Data Pipeline\n\nProcess large datasets efficiently.', 123, 15),
('ml-toolkit', 'ml-toolkit', 'Machine learning toolkit and utilities', 1, 4, 'public', 'develop', 'Python', 'ml,ai,tensorflow', '# ML Toolkit\n\nMachine learning utilities.', 892, 145),
('cloud-infra', 'cloud-infra', 'Cloud infrastructure automation', 1, 4, 'private', 'main', 'Terraform', 'cloud,devops,infrastructure', '# Cloud Infrastructure\n\nAutomated cloud provisioning.', 78, 9),
('ui-components', 'ui-components', 'Reusable UI component library', 1, 5, 'public', 'main', 'TypeScript', 'ui,components,react', '# UI Components\n\nBeautiful, reusable components.', 1234, 234),
('monitoring-tool', 'monitoring-tool', 'Application monitoring and observability', 1, 5, 'public', 'main', 'Go', 'monitoring,observability,metrics', '# Monitoring Tool\n\nComprehensive monitoring solution.', 445, 67);

-- Additional Labels for repositories
INSERT INTO label (repository_id, name, color, description) VALUES
-- demo-repo (1)
(1, 'documentation', '#0075ca', 'Improvements or additions to documentation'),
(1, 'good first issue', '#7057ff', 'Good for newcomers'),
(1, 'help wanted', '#008672', 'Extra attention is needed'),
-- web-framework (2)
(2, 'bug', '#d73a4a', 'Something is not working'),
(2, 'enhancement', '#a2eeef', 'New feature or request'),
(2, 'performance', '#f9d0c4', 'Performance improvements'),
(2, 'security', '#ee0701', 'Security vulnerability'),
-- mobile-app (3)
(3, 'bug', '#d73a4a', 'Something is not working'),
(3, 'ui/ux', '#d876e3', 'User interface improvements'),
(3, 'android', '#3ddc84', 'Android specific'),
(3, 'ios', '#999999', 'iOS specific'),
-- api-gateway (4)
(4, 'bug', '#d73a4a', 'Something is not working'),
(4, 'enhancement', '#a2eeef', 'New feature or request'),
(4, 'breaking change', '#b60205', 'Breaking API change'),
-- ml-toolkit (5)
(5, 'bug', '#d73a4a', 'Something is not working'),
(5, 'model', '#fbca04', 'Model improvements'),
(5, 'dataset', '#c5def5', 'Dataset related'),
-- ui-components (6)
(6, 'bug', '#d73a4a', 'Something is not working'),
(6, 'component', '#1d76db', 'New component request'),
(6, 'accessibility', '#e99695', 'Accessibility improvements');

-- Additional Milestones
INSERT INTO milestone (repository_id, title, description, state, due_date) VALUES
(1, 'v2.0', 'Major version 2 release', 'open', '2025-12-31'),
(2, 'v1.0', 'First stable release', 'open', '2025-06-30'),
(2, 'v1.1', 'Minor improvements', 'open', '2025-09-30'),
(3, 'Beta Release', 'Beta version for testing', 'open', '2025-05-15'),
(4, 'v1.0', 'Production ready', 'open', '2025-08-01'),
(5, 'Model v2', 'Improved model architecture', 'open', '2025-07-15'),
(6, 'Component Library v1', 'Complete component set', 'open', '2025-06-01');

-- Additional Issues
INSERT INTO issue (repository_id, issue_number, title, description, state, issue_type, priority, author_id, assignee_id, milestone_id) VALUES
-- demo-repo issues
(1, 2, 'Add user authentication', 'Implement JWT-based authentication system', 'open', 'enhancement', 'high', 1, NULL, 2),
(1, 3, 'Fix memory leak in data processing', 'Memory usage increases over time during batch processing', 'open', 'bug', 'critical', 1, NULL, 1),
(1, 4, 'Improve API documentation', 'Add more examples and use cases to API docs', 'in_progress', 'documentation', 'medium', 1, NULL, 2),
(1, 5, 'Performance optimization for queries', 'Database queries are slow with large datasets', 'open', 'enhancement', 'high', 1, NULL, 2),
-- web-framework issues
(2, 1, 'Router not handling nested routes', 'Nested routes fail to render properly', 'open', 'bug', 'high', 1, NULL, 1),
(2, 2, 'Add middleware support', 'Support for custom middleware functions', 'open', 'enhancement', 'medium', 1, NULL, 1),
(2, 3, 'Improve error messages', 'Error messages are not descriptive enough', 'in_progress', 'enhancement', 'low', 1, NULL, 2),
(2, 4, 'Security: XSS vulnerability in forms', 'Form inputs not properly sanitized', 'open', 'bug', 'critical', 1, NULL, 1),
-- mobile-app issues
(3, 1, 'App crashes on Android 14', 'Application crashes on startup for Android 14 devices', 'open', 'bug', 'critical', 1, NULL, 1),
(3, 2, 'Add dark mode support', 'Implement dark theme throughout the app', 'open', 'enhancement', 'medium', 1, NULL, NULL),
(3, 3, 'Push notifications not working on iOS', 'iOS devices not receiving push notifications', 'in_progress', 'bug', 'high', 1, NULL, 1),
-- api-gateway issues
(4, 1, 'Add rate limiting per endpoint', 'Need configurable rate limits for different endpoints', 'open', 'enhancement', 'high', 1, NULL, 1),
(4, 2, 'WebSocket support', 'Add WebSocket protocol support', 'open', 'enhancement', 'medium', 1, NULL, NULL),
(4, 3, 'Load balancer health checks failing', 'Health check endpoint returns incorrect status', 'open', 'bug', 'high', 1, NULL, 1),
-- ml-toolkit issues
(5, 1, 'Model training fails with large datasets', 'Out of memory errors during training', 'open', 'bug', 'high', 1, NULL, 1),
(5, 2, 'Add support for GPU acceleration', 'Implement CUDA support for faster training', 'open', 'enhancement', 'high', 1, NULL, 1),
(5, 3, 'Visualization tools for model performance', 'Add charts and graphs for model metrics', 'in_progress', 'enhancement', 'medium', 1, NULL, 1),
-- ui-components issues
(6, 1, 'DatePicker component keyboard navigation', 'Cannot navigate DatePicker with keyboard', 'open', 'bug', 'medium', 1, NULL, 1),
(6, 2, 'Add Table component', 'Need a fully-featured table component', 'open', 'enhancement', 'high', 1, NULL, 1),
(6, 3, 'Button component focus outline', 'Focus outline not visible in high contrast mode', 'open', 'bug', 'low', 1, NULL, 1);

-- Link issues to labels
INSERT INTO issue_label (issue_id, label_id) VALUES
(1, 2), -- Sample issue - enhancement
(2, 2), -- Add user authentication - enhancement
(3, 1), -- Fix memory leak - bug
(4, 3), -- Improve API documentation - documentation
(5, 2), -- Performance optimization - enhancement
(6, 5), -- Router not handling nested routes - bug
(7, 6), -- Add middleware support - enhancement
(9, 8), -- Security: XSS vulnerability - security
(10, 10), -- App crashes on Android 14 - bug
(11, 11), -- Add dark mode support - ui/ux
(12, 12), -- Push notifications not working on iOS - ios
(13, 10), -- App crashes - bug (mobile-app)
(14, 15), -- Add rate limiting - bug (api-gateway)
(15, 16), -- WebSocket support - enhancement (api-gateway)
(18, 19), -- Model training fails - bug (ml-toolkit)
(19, 19), -- Add GPU support - bug (ml-toolkit)
(20, 20), -- Visualization tools - model
(21, 23), -- DatePicker keyboard navigation - bug (ui-components)
(22, 24), -- Add Table component - component
(23, 25); -- Button focus outline - accessibility

-- Additional Pull Requests
INSERT INTO pull_request (repository_id, pr_number, title, description, state, source_branch, target_branch, author_id, assignee_id, milestone_id, is_draft) VALUES
(1, 2, 'Feature: Add Redis caching', 'Implements Redis caching layer for improved performance', 'open', 'feature/redis-cache', 'main', 1, NULL, 2, 0),
(1, 3, 'Fix: Resolve race condition in async handlers', 'Fixes race condition that caused data corruption', 'merged', 'fix/race-condition', 'main', 1, NULL, 1, 0),
(2, 1, 'Feature: Middleware system', 'Adds support for custom middleware', 'open', 'feature/middleware', 'main', 1, NULL, 1, 0),
(2, 2, 'Docs: Update getting started guide', 'Improves documentation for new users', 'open', 'docs/getting-started', 'main', 1, NULL, 2, 0),
(3, 1, 'Fix: Android 14 compatibility', 'Fixes crash on Android 14 devices', 'open', 'fix/android-14', 'main', 1, NULL, 1, 0),
(3, 2, 'WIP: Dark mode implementation', 'Work in progress dark mode support', 'open', 'feature/dark-mode', 'main', 1, NULL, NULL, 1),
(4, 1, 'Feature: Rate limiting', 'Implements rate limiting per endpoint', 'open', 'feature/rate-limit', 'main', 1, NULL, 1, 0),
(5, 1, 'Feature: GPU acceleration', 'Adds CUDA support for model training', 'open', 'feature/gpu-support', 'develop', 1, NULL, 1, 0),
(6, 1, 'Feature: Table component', 'New table component with sorting and filtering', 'open', 'feature/table-component', 'main', 1, NULL, 1, 0);

-- Link pull requests to labels
INSERT INTO pull_request_label (pull_request_id, label_id) VALUES
(1, 2), -- Sample PR - enhancement
(2, 2), -- Add Redis caching - enhancement
(4, 6), -- Middleware system - enhancement (web-framework)
(5, 3), -- Update getting started - documentation
(6, 10), -- Android 14 compatibility - bug (mobile-app)
(7, 11), -- Dark mode - ui/ux (mobile-app)
(8, 16), -- Rate limiting - enhancement (api-gateway)
(9, 19), -- GPU acceleration - bug (ml-toolkit)
(10, 24); -- Table component - component (ui-components)

-- Additional Comments
INSERT INTO comment (entity_type, entity_id, author_id, content) VALUES
-- Issue comments
('issue', 1, 1, 'This is a critical issue that needs immediate attention.'),
('issue', 1, 1, 'I''ve started working on a fix. Will have a PR ready soon.'),
('issue', 2, 1, 'JWT seems like the right approach. Should we also consider OAuth2?'),
('issue', 3, 1, 'Reproduced the issue. Memory leak is in the batch processor.'),
('issue', 3, 1, 'Created a heap dump for analysis.'),
('issue', 6, 1, 'This is blocking the v1.0 release.'),
('issue', 10, 1, 'Confirmed on Android 14. Works fine on Android 13.'),
('issue', 14, 1, 'Suggesting a token bucket algorithm for rate limiting.'),
('issue', 18, 1, 'Need to implement batch processing for large datasets.'),
-- Pull request comments
('pull_request', 2, 1, 'Great work! Just a few minor comments on the implementation.'),
('pull_request', 2, 1, 'Please add tests for the cache invalidation logic.'),
('pull_request', 3, 1, 'LGTM! Merging now.'),
('pull_request', 4, 1, 'The middleware API looks clean. Nice job!'),
('pull_request', 6, 1, 'This is still a work in progress. Don''t merge yet.');

-- Additional Wiki Pages
INSERT INTO wiki_page (repository_id, title, content) VALUES
(1, 'Home', '# Welcome to Demo Repository\n\nThis is the home page of our wiki.'),
(1, 'Installation', '# Installation Guide\n\n## Prerequisites\n- Java 21\n- Maven 3.8+\n\n## Steps\n1. Clone the repository\n2. Run `mvn clean install`'),
(1, 'Architecture', '# Architecture Overview\n\nOur application follows a layered architecture pattern.'),
(2, 'Home', '# Web Framework Documentation\n\nWelcome to the official documentation.'),
(2, 'Getting Started', '# Getting Started\n\n```javascript\nimport { Framework } from ''web-framework'';\n```'),
(4, 'Home', '# API Gateway Wiki\n\nDocumentation for the API Gateway project.'),
(4, 'Configuration', '# Configuration Guide\n\nHow to configure the gateway for your needs.'),
(6, 'Home', '# UI Components Library\n\nReusable components for modern applications.');

-- Additional Git Commits
INSERT INTO git_commit (repository_id, hash, message, author_name, author_email) VALUES
(1, 'a1b2c3d4e5f6', 'Initial commit', 'John Doe', 'john@example.com'),
(1, 'f6e5d4c3b2a1', 'Add user authentication', 'John Doe', 'john@example.com'),
(1, 'b2c3d4e5f6a1', 'Fix memory leak', 'John Doe', 'john@example.com'),
(2, 'c3d4e5f6a1b2', 'Setup project structure', 'Jane Smith', 'jane@example.com'),
(2, 'd4e5f6a1b2c3', 'Implement routing system', 'Jane Smith', 'jane@example.com'),
(3, 'e5f6a1b2c3d4', 'Initial Android app', 'Bob Johnson', 'bob@example.com'),
(4, 'f6a1b2c3d4e5', 'API gateway foundation', 'Alice Williams', 'alice@example.com'),
(5, 'a1b2c3d4e5f7', 'First ML model', 'Charlie Brown', 'charlie@example.com');

-- Additional Git Branches
INSERT INTO git_branch (repository_id, name, head_commit_id) VALUES
(1, 'main', 3),
(1, 'develop', 3),
(1, 'feature/redis-cache', 2),
(2, 'main', 5),
(2, 'feature/middleware', 5),
(3, 'main', 6),
(3, 'feature/dark-mode', 6),
(4, 'main', 7),
(5, 'develop', 8);

-- Additional Repository Stars
INSERT INTO repository_star (user_id, repository_id) VALUES
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7);

-- Additional Notifications
INSERT INTO notification (user_id, title, message, link, is_read) VALUES
(1, 'New issue assigned', 'Issue #3 has been assigned to you', '/repository/1/issue/3', 0),
(1, 'Pull request review requested', 'Review requested for PR #2', '/repository/1/pr/2', 0),
(1, 'Issue commented', 'Someone commented on your issue #1', '/repository/1/issue/1', 1),
(1, 'Build failed', 'Build failed for branch feature/redis-cache', '/repository/1/build/42', 0),
(1, 'New star', 'Your repository received a new star', '/repository/2', 1),
(1, 'Pull request merged', 'Your PR #3 has been merged', '/repository/1/pr/3', 1);

-- Additional Organization Members
INSERT INTO organization_member (organization_id, user_id, role) VALUES
(2, 1, 'owner'),
(3, 1, 'owner'),
(4, 1, 'owner'),
(5, 1, 'owner');

-- Additional Repository Collaborators
INSERT INTO repository_collaborator (repository_id, user_id, permission) VALUES
(2, 1, 'admin'),
(3, 1, 'write'),
(4, 1, 'admin'),
(5, 1, 'admin'),
(6, 1, 'write'),
(7, 1, 'admin'),
(8, 1, 'admin');
