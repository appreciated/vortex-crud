DROP TABLE IF EXISTS pdf_documents;

CREATE TABLE pdf_documents (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    url VARCHAR(255)
);

INSERT INTO pdf_documents (title, url) VALUES ('Sample PDF', 'sample.pdf');
INSERT INTO pdf_documents (title, url) VALUES ('Document', 'document.pdf');
