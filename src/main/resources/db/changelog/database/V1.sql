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
CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          comment_text VARCHAR(1000),
                          employee_id INT,
                          created_at TIMESTAMP DEFAULT NOW()
);

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
    ('Design Homepage', 'Create the design for the homepage of the web app', 1, 'In Progress', '2023-12-01', NOW(), NOW()),
    ('Database Setup', 'Set up the database structure for Project Beta', 2, 'Completed', '2023-10-01', NOW(), NOW()),
    ('AI Model Training', 'Start training the AI model for Project Gamma', 3, 'Not Started', '2024-03-15', NOW(), NOW());

-- Insert example data into comments table
INSERT INTO comments (comment_text, employee_id, created_at)
VALUES
    ('We need to finalize the design by the end of the week.', 1, NOW()),
    ('The database structure is ready for review.', 2, NOW()),
    ('Waiting for more data to begin AI model training.', 3, NOW());
