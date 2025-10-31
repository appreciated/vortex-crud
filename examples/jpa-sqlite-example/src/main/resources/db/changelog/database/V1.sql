-- liquibase formatted sql

-- changeset jpa-sqlite-example-vortex-crud:1
CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

-- changeset jpa-sqlite-example-vortex-crud:2
CREATE TABLE tasks
(
    id          INTEGER PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    assigned_to INT,
    status      VARCHAR(50),
    due_date    DATE,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    row_index   INTEGER
);

-- changeset jpa-sqlite-example-vortex-crud:3
CREATE TABLE task_has_task
(
    id              INTEGER PRIMARY KEY,
    task_id         INT NOT NULL,
    related_task_id INT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (task_id, related_task_id),
    FOREIGN KEY (related_task_id) REFERENCES tasks (id) ON DELETE RESTRICT,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE RESTRICT
);

-- changeset jpa-sqlite-example-vortex-crud:4
CREATE TABLE task_comments
(
    id           INTEGER PRIMARY KEY,
    comment_text VARCHAR(1000),
    user_id      INT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    task_id      INT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE RESTRICT
);

-- changeset jpa-sqlite-example-vortex-crud:5
CREATE TABLE images
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR,
    url   VARCHAR
);

-- changeset jpa-sqlite-example-test-project:5.1
CREATE TABLE videos
(
    id    INTEGER PRIMARY KEY,
    title VARCHAR,
    url   VARCHAR
);

-- changeset jpa-sqlite-example-vortex-crud:7
INSERT INTO images (id, title, url)
VALUES (1, 'Red', './images/red.png'),
       (2, 'Green', './images/green.png'),
       (3, 'Blue', './images/blue.png');

-- changeset jpa-sqlite-example-vortex-crud:8
INSERT INTO projects (name, description, start_date, end_date, created_at, updated_at)
VALUES ('Project Alpha', 'A high-priority project aimed at improving the internal system', '2023-01-01 00:00:00.000',
        '2023-12-31 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Beta', 'Developing a new customer-facing web application', '2023-05-15 00:00:00.000',
        '2024-06-30 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Gamma', 'Research and development of a new AI model', '2024-01-01 00:00:00.000',
        '2024-12-31 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Delta', 'Upgrade of legacy systems to modern infrastructure', '2023-02-01 00:00:00.000',
        '2023-10-31 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Epsilon', 'Migration of data from old to new database systems', '2023-03-01 00:00:00.000',
        '2023-09-30 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Zeta', 'Implementation of a company-wide CRM system', '2023-04-15 00:00:00.000',
        '2023-11-15 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Eta', 'Development of a machine learning model for predictive analytics', '2023-06-01 00:00:00.000',
        '2024-05-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Theta', 'Rebranding and marketing strategy for new products', '2023-07-01 00:00:00.000',
        '2024-02-28 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Iota', 'Development of a mobile application for internal communication', '2023-08-01 00:00:00.000',
        '2024-03-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Kappa', 'Creation of an automated customer service chatbot', '2023-09-01 00:00:00.000',
        '2024-04-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Lambda', 'Integration of new API systems for external partners', '2023-10-01 00:00:00.000',
        '2024-05-31 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Mu', 'Development of an AI-powered recommendation engine', '2023-11-01 00:00:00.000',
        '2024-06-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Nu', 'Implementation of cybersecurity protocols across all systems', '2023-12-01 00:00:00.000',
        '2024-07-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Xi', 'Redesign of the company website for better user experience', '2024-01-01 00:00:00.000',
        '2024-08-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Omicron', 'Developing a blockchain-based data storage solution', '2024-02-01 00:00:00.000',
        '2024-09-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Pi', 'Optimization of the supply chain management system', '2024-03-01 00:00:00.000',
        '2024-10-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Rho', 'Building an analytics dashboard for executive reporting', '2024-04-01 00:00:00.000',
        '2024-11-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Sigma', 'Development of a social media monitoring tool', '2024-05-01 00:00:00.000',
        '2024-12-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Tau', 'Research and development of a voice recognition system', '2024-06-01 00:00:00.000',
        '2025-01-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Upsilon', 'Creation of a new HR management system', '2024-07-01 00:00:00.000',
        '2025-02-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Phi', 'Implementation of a new financial management platform', '2024-08-01 00:00:00.000',
        '2025-03-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Chi', 'Development of a video conferencing platform for remote teams', '2024-09-01 00:00:00.000',
        '2025-04-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Psi', 'Building a customer loyalty program based on AI insights', '2024-10-01 00:00:00.000',
        '2025-05-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Omega', 'Research into the development of a quantum computing algorithm', '2024-11-01 00:00:00.000',
        '2025-06-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Alpha 2.0', 'Next phase of the internal system improvement project', '2024-12-01 00:00:00.000',
        '2025-07-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Beta 2.0', 'Expansion of the customer-facing web application for global markets',
        '2025-01-01 00:00:00.000', '2025-08-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Gamma 2.0', 'Enhancements to the AI model with additional features', '2025-02-01 00:00:00.000',
        '2025-09-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Delta 2.0', 'Further upgrades to the modern infrastructure', '2025-03-01 00:00:00.000',
        '2025-10-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Epsilon 2.0', 'Refinement of data migration processes', '2025-04-01 00:00:00.000',
        '2025-11-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Zeta 2.0', 'Phase two of the CRM system implementation', '2025-05-01 00:00:00.000',
        '2025-12-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- changeset jpa-sqlite-example-test-project:9
INSERT INTO tasks (title, description, assigned_to, status, due_date, created_at, updated_at, row_index)
VALUES ('Design Homepage', 'Create the design for the homepage of the web app', 1, 'WORK_IN_PROGRESS',
        '2023-12-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000),
       ('Database Setup', 'Set up the database structure for Project Beta', 2, 'CLOSED', '2023-10-01 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000),
       ('AI Model Training', 'Start training the AI model for Project Gamma', 3, 'OPEN', '2024-03-15 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1000),
       ('User Authentication', 'Implement the user authentication module', 4, 'WORK_IN_PROGRESS',
        '2023-11-20 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000),
       ('Frontend Integration', 'Integrate the frontend with the backend API', 5, 'WORK_IN_PROGRESS',
        '2023-12-10 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3000),
       ('Bug Fixes', 'Fix critical bugs reported by the QA team', 6, 'WORK_IN_PROGRESS', '2023-10-15 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4000),
       ('API Documentation', 'Write documentation for the REST API endpoints', 7, 'OPEN', '2023-11-05 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000),
       ('Performance Optimization', 'Optimize the database queries for faster performance', 2, 'WORK_IN_PROGRESS',
        '2023-12-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5000),
       ('Code Review', 'Review the codebase for Project Alpha', 4, 'CLOSED', '2023-09-25 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2000),
       ('UI Testing', 'Perform user interface testing for the mobile app', 5, 'WORK_IN_PROGRESS',
        '2023-11-30 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6000),
       ('Security Audit', 'Conduct a security audit for Project Delta', 3, 'WORK_IN_PROGRESS',
        '2024-01-15 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7000),
       ('Marketing Campaign', 'Plan the marketing campaign for product launch', 8, 'OPEN', '2024-02-01 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3000),
       ('Data Migration', 'Migrate old data to the new database schema', 2, 'CLOSED', '2023-10-05 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3000),
       ('Client Meeting', 'Schedule a meeting with the client to discuss requirements', 1, 'OPEN',
        '2023-10-18 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4000),
       ('Testing Framework Setup', 'Set up a testing framework for the new microservices', 6, 'OPEN',
        '2023-12-15 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5000),
       ('Content Update', 'Update the content on the landing page', 7, 'CLOSED', '2023-09-28 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4000),
       ('Payment Gateway Integration', 'Integrate the payment gateway for online transactions', 4, 'WORK_IN_PROGRESS',
        '2023-11-25 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 8000),
       ('Server Maintenance', 'Perform maintenance on the main server', 5, 'OPEN', '2023-10-22 00:00:00.000',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 6000),
       ('UI Enhancements', 'Implement new UI enhancements based on user feedback', 3, 'WORK_IN_PROGRESS',
        '2024-01-05 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 9000),
       ('SEO Optimization', 'Optimize the website for better search engine rankings', 8, 'OPEN',
        '2024-03-01 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 7000),
       ('Mobile App Deployment', 'Deploy the mobile app to the App Store and Play Store', 7, 'CLOSED',
        '2023-10-10 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5000),
       ('Email Notifications', 'Set up automated email notifications for user actions', 2, 'WORK_IN_PROGRESS',
        '2023-12-20 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 10000),
       ('Accessibility Review', 'Review the app for accessibility compliance', 4, 'WORK_IN_PROGRESS',
        '2023-11-17 00:00:00.000', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 11000);

-- changeset jpa-sqlite-example-vortex-crud:10
INSERT INTO task_comments (comment_text, user_id, created_at, task_id)
VALUES ('We need to finalize the design by the end of the week.', 1, CURRENT_TIMESTAMP, 1),
       ('The database structure is ready for review.', 2, CURRENT_TIMESTAMP, 2),
       ('Waiting for more data to begin AI model training.', 3, CURRENT_TIMESTAMP, 3);

-- changeset jpa-sqlite-example-vortex-crud:11
INSERT INTO task_has_task (task_id, related_task_id)
VALUES (1, 2), -- "Database Setup"
       (1, 4), -- "User Authentication"
       (1, 5); -- "Frontend Integration"