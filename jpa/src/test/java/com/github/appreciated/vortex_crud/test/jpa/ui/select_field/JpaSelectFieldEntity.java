package com.github.appreciated.vortex_crud.test.jpa.ui.select_field;

import com.github.appreciated.vortex_crud.jpa.service.annoations.SelectField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
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
}
