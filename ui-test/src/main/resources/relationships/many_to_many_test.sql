-- Many-to-many relationship test for JPA
CREATE TABLE IF NOT EXISTS jpa_students (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS jpa_courses (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    credits INTEGER
);

CREATE TABLE IF NOT EXISTS jpa_student_course (
    student_id INTEGER,
    course_id INTEGER,
    enrollment_date DATE DEFAULT CURRENT_DATE,
    grade VARCHAR(2),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES jpa_students(id),
    FOREIGN KEY (course_id) REFERENCES jpa_courses(id)
);

-- Insert test data
INSERT INTO jpa_students (id, name, email)
VALUES 
    (1, 'Alex Johnson', 'alex@university.edu'),
    (2, 'Maria Garcia', 'maria@university.edu'),
    (3, 'Sam Taylor', 'sam@university.edu'),
    (4, 'Olivia Brown', 'olivia@university.edu');

INSERT INTO jpa_courses (id, title, description, credits)
VALUES 
    (1, 'Database Systems', 'Introduction to database design and SQL', 3),
    (2, 'Web Development', 'Building modern web applications', 4),
    (3, 'Data Structures', 'Fundamental data structures and algorithms', 4),
    (4, 'Machine Learning', 'Introduction to machine learning concepts', 3);

INSERT INTO jpa_student_course (student_id, course_id, enrollment_date, grade)
VALUES 
    (1, 1, '2023-09-01', 'A'),
    (1, 2, '2023-09-01', 'B'),
    (1, 3, '2023-09-01', NULL),
    (2, 1, '2023-09-01', 'A'),
    (2, 4, '2023-09-01', 'A'),
    (3, 2, '2023-09-01', 'C'),
    (3, 3, '2023-09-01', 'B'),
    (4, 1, '2023-09-01', 'B'),
    (4, 2, '2023-09-01', 'A'),
    (4, 4, '2023-09-01', NULL);