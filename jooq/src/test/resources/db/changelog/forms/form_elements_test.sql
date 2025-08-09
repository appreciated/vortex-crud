-- Form elements test for JPA
CREATE TABLE IF NOT EXISTS jpa_form_elements (
    id INTEGER PRIMARY KEY,
    text_field VARCHAR(255) NOT NULL,
    text_area TEXT,
    number_field INTEGER,
    date_field DATE,
    time_field TIME,
    datetime_field TIMESTAMP,
    checkbox_field BOOLEAN DEFAULT FALSE,
    select_field VARCHAR(50) CHECK (select_field IN ('OPTION1', 'OPTION2', 'OPTION3')),
    radio_field VARCHAR(50),
    email_field VARCHAR(255),
    password_field VARCHAR(255),
    url_field VARCHAR(255),
    color_field VARCHAR(7),
    range_field INTEGER CHECK (range_field BETWEEN 1 AND 100)
);

-- Insert test data
INSERT INTO jpa_form_elements (
    id, text_field, text_area, number_field, date_field, 
    time_field, datetime_field, checkbox_field, select_field, 
    radio_field, email_field, password_field, url_field, 
    color_field, range_field
)
VALUES 
    (
        1, 
        'Sample Text', 
        'This is a longer text that would go in a text area field.', 
        42, 
        '2023-05-15', 
        '14:30:00', 
        '2023-05-15 14:30:00', 
        TRUE, 
        'OPTION1', 
        'Radio1', 
        'test@example.com', 
        'hashedpassword123', 
        'https://example.com', 
        '#FF5733', 
        75
    ),
    (
        2, 
        'Another Text', 
        'Another example of text area content with multiple lines of text.', 
        100, 
        '2023-06-20', 
        '09:15:00', 
        '2023-06-20 09:15:00', 
        FALSE, 
        'OPTION2', 
        'Radio2', 
        'another@example.com', 
        'hashedpassword456', 
        'https://another-example.com', 
        '#33FF57', 
        25
    );