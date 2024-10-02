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
                       status VARCHAR(50) CHECK(status IN ('task_status')), -- assuming 'task_status' is a predefined set of values
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
