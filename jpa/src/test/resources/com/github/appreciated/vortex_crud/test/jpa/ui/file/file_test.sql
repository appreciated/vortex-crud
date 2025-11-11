DROP TABLE IF EXISTS generic_files;

CREATE TABLE generic_files (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    url VARCHAR(255)
);

INSERT INTO generic_files (name, url) VALUES ('Sample Document', 'sample.docx');
INSERT INTO generic_files (name, url) VALUES ('Spreadsheet', 'data.xlsx');
