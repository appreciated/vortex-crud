-- Table: projects
CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(500),
                          start_date DATE,
                          end_date DATE,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP
);

-- Table: tasks
CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(1000),
                       assigned_to INT,
                       status VARCHAR(50),
                       due_date DATE,
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);

-- Table: comments
-- Table: task_comments
CREATE TABLE task_comments (
                       id SERIAL PRIMARY KEY,
                       comment_text VARCHAR(1000),
                       user_id INT,
                       FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
                       created_at TIMESTAMP DEFAULT NOW(),
                       task_id INT,
                       FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE RESTRICT
);

INSERT INTO users (id, username)
VALUES
    (1, 'max@mustermann.de'),
    (2, 'erika@musterfrau.de'),
    (3, 'john@doe.com'),
    (4, 'jane@doe.com');

-- Insert example data into projects table
INSERT INTO projects (name, description, start_date, end_date, created_at, updated_at)
VALUES
    ('Project Alpha', 'A high-priority project aimed at improving the internal system', '2023-01-01', '2023-12-31', NOW(), NOW()),
    ('Project Beta', 'Developing a new customer-facing web application', '2023-05-15', '2024-06-30', NOW(), NOW()),
    ('Project Gamma', 'Research and development of a new AI model', '2024-01-01', '2024-12-31', NOW(), NOW()),
    ('Project Delta', 'Upgrade of legacy systems to modern infrastructure', '2023-02-01', '2023-10-31', NOW(), NOW()),
    ('Project Epsilon', 'Migration of data from old to new database systems', '2023-03-01', '2023-09-30', NOW(), NOW()),
    ('Project Zeta', 'Implementation of a company-wide CRM system', '2023-04-15', '2023-11-15', NOW(), NOW()),
    ('Project Eta', 'Development of a machine learning model for predictive analytics', '2023-06-01', '2024-05-01', NOW(), NOW()),
    ('Project Theta', 'Rebranding and marketing strategy for new products', '2023-07-01', '2024-02-28', NOW(), NOW()),
    ('Project Iota', 'Development of a mobile application for internal communication', '2023-08-01', '2024-03-01', NOW(), NOW()),
    ('Project Kappa', 'Creation of an automated customer service chatbot', '2023-09-01', '2024-04-01', NOW(), NOW()),
    ('Project Lambda', 'Integration of new API systems for external partners', '2023-10-01', '2024-05-31', NOW(), NOW()),
    ('Project Mu', 'Development of an AI-powered recommendation engine', '2023-11-01', '2024-06-01', NOW(), NOW()),
    ('Project Nu', 'Implementation of cybersecurity protocols across all systems', '2023-12-01', '2024-07-01', NOW(), NOW()),
    ('Project Xi', 'Redesign of the company website for better user experience', '2024-01-01', '2024-08-01', NOW(), NOW()),
    ('Project Omicron', 'Developing a blockchain-based data storage solution', '2024-02-01', '2024-09-01', NOW(), NOW()),
    ('Project Pi', 'Optimization of the supply chain management system', '2024-03-01', '2024-10-01', NOW(), NOW()),
    ('Project Rho', 'Building an analytics dashboard for executive reporting', '2024-04-01', '2024-11-01', NOW(), NOW()),
    ('Project Sigma', 'Development of a social media monitoring tool', '2024-05-01', '2024-12-01', NOW(), NOW()),
    ('Project Tau', 'Research and development of a voice recognition system', '2024-06-01', '2025-01-01', NOW(), NOW()),
    ('Project Upsilon', 'Creation of a new HR management system', '2024-07-01', '2025-02-01', NOW(), NOW()),
    ('Project Phi', 'Implementation of a new financial management platform', '2024-08-01', '2025-03-01', NOW(), NOW()),
    ('Project Chi', 'Development of a video conferencing platform for remote teams', '2024-09-01', '2025-04-01', NOW(), NOW()),
    ('Project Psi', 'Building a customer loyalty program based on AI insights', '2024-10-01', '2025-05-01', NOW(), NOW()),
    ('Project Omega', 'Research into the development of a quantum computing algorithm', '2024-11-01', '2025-06-01', NOW(), NOW()),
    ('Project Alpha 2.0', 'Next phase of the internal system improvement project', '2024-12-01', '2025-07-01', NOW(), NOW()),
    ('Project Beta 2.0', 'Expansion of the customer-facing web application for global markets', '2025-01-01', '2025-08-01', NOW(), NOW()),
    ('Project Gamma 2.0', 'Enhancements to the AI model with additional features', '2025-02-01', '2025-09-01', NOW(), NOW()),
    ('Project Delta 2.0', 'Further upgrades to the modern infrastructure', '2025-03-01', '2025-10-01', NOW(), NOW()),
    ('Project Epsilon 2.0', 'Refinement of data migration processes', '2025-04-01', '2025-11-01', NOW(), NOW()),
    ('Project Zeta 2.0', 'Phase two of the CRM system implementation', '2025-05-01', '2025-12-01', NOW(), NOW());

-- Insert example data into tasks table
INSERT INTO tasks (title, description, assigned_to, status, due_date, created_at, updated_at)
VALUES
    ('Design Homepage', 'Create the design for the homepage of the web app', 1, 'work-in-progress', '2023-12-01', NOW(), NOW()),
    ('Database Setup', 'Set up the database structure for Project Beta', 2, 'closed', '2023-10-01', NOW(), NOW()),
    ('AI Model Training', 'Start training the AI model for Project Gamma', 3, 'open', '2024-03-15', NOW(), NOW()),
    ('User Authentication', 'Implement the user authentication module', 4, 'work-in-progress', '2023-11-20', NOW(), NOW()),
    ('Frontend Integration', 'Integrate the frontend with the backend API', 5, 'work-in-progress', '2023-12-10', NOW(), NOW()),
    ('Bug Fixes', 'Fix critical bugs reported by the QA team', 6, 'work-in-progress', '2023-10-15', NOW(), NOW()),
    ('API Documentation', 'Write documentation for the REST API endpoints', 7, 'open', '2023-11-05', NOW(), NOW()),
    ('Performance Optimization', 'Optimize the database queries for faster performance', 2, 'work-in-progress', '2023-12-01', NOW(), NOW()),
    ('Code Review', 'Review the codebase for Project Alpha', 4, 'closed', '2023-09-25', NOW(), NOW()),
    ('UI Testing', 'Perform user interface testing for the mobile app', 5, 'work-in-progress', '2023-11-30', NOW(), NOW()),
    ('Security Audit', 'Conduct a security audit for Project Delta', 3, 'work-in-progress', '2024-01-15', NOW(), NOW()),
    ('Marketing Campaign', 'Plan the marketing campaign for product launch', 8, 'open', '2024-02-01', NOW(), NOW()),
    ('Data Migration', 'Migrate old data to the new database schema', 2, 'closed', '2023-10-05', NOW(), NOW()),
    ('Client Meeting', 'Schedule a meeting with the client to discuss requirements', 1, 'open', '2023-10-18', NOW(), NOW()),
    ('Testing Framework Setup', 'Set up a testing framework for the new microservices', 6, 'open', '2023-12-15', NOW(), NOW()),
    ('Content Update', 'Update the content on the landing page', 7, 'closed', '2023-09-28', NOW(), NOW()),
    ('Payment Gateway Integration', 'Integrate the payment gateway for online transactions', 4, 'work-in-progress', '2023-11-25', NOW(), NOW()),
    ('Server Maintenance', 'Perform maintenance on the main server', 5, 'open', '2023-10-22', NOW(), NOW()),
    ('UI Enhancements', 'Implement new UI enhancements based on user feedback', 3, 'work-in-progress', '2024-01-05', NOW(), NOW()),
    ('SEO Optimization', 'Optimize the website for better search engine rankings', 8, 'open', '2024-03-01', NOW(), NOW()),
    ('Mobile App Deployment', 'Deploy the mobile app to the App Store and Play Store', 7, 'closed', '2023-10-10', NOW(), NOW()),
    ('Email Notifications', 'Set up automated email notifications for user actions', 2, 'work-in-progress', '2023-12-20', NOW(), NOW()),
    ('Accessibility Review', 'Review the app for accessibility compliance', 4, 'work-in-progress', '2023-11-17', NOW(), NOW()),
    ('Cloud Migration', 'Migrate services to the new cloud provider', 6, 'work-in-progress', '2024-02-10', NOW(), NOW());

-- Insert example data into comments table
INSERT INTO task_comments (comment_text, user_id, created_at)
VALUES
    ('We need to finalize the design by the end of the week.', 1, NOW()),
    ('The database structure is ready for review.', 2, NOW()),
    ('Waiting for more data to begin AI model training.', 3, NOW());
