package com.github.appreciated.vortex_crud.test.jpa.ui.global_route_action;

import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.MultiSelectValueField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.PdfField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "global_route_action_test")
public class JpaGlobalRouteActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @TextField
    @Column(name = "name")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "missing_features_test_tags", joinColumns = @JoinColumn(name = "entity_id"))
    @Column(name = "tag_value")
    @MultiSelectValueField("tags")
    private Set<String> tags;

    @PdfField(LocalPdfResourceProvider.class)
    @Column(name = "pdf_doc")
    private String pdfDoc;

    @Column(name = "notes", columnDefinition = "TEXT")
    @TextAreaField
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
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
