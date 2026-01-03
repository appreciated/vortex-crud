package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import com.github.appreciated.vortex_crud.jpa.service.annoations.SelectField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField;
import jakarta.persistence.*;

@Entity
@Table(name = "select_field_test")
public class JpaSelectFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    @SelectField(value = "name-options")
    private String name;

    @Column(name = "pdf_doc")
    @TextField
    private String pdfDoc;

    @Column(name = "notes")
    @TextAreaField
    private String notes;

    // Default constructor
    public JpaSelectFieldEntity() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfDoc() {
        return pdfDoc;
    }

    public void setPdfDoc(String pdfDoc) {
        this.pdfDoc = pdfDoc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
