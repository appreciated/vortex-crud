package com.github.appreciated.vortex_crud.test.jpa.ui.missing_features;

import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "missing_features_test")
public class JpaMissingFeaturesEntity {
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

    @com.github.appreciated.vortex_crud.jpa.service.annoations.DateRangeField
    @Convert(converter = com.github.appreciated.vortex_crud.jpa.service.converter.DateRangeConverter.class)
    private com.github.appreciated.vortex_crud.core.entity.DateRange dateRange;

    @com.github.appreciated.vortex_crud.jpa.service.annoations.DateTimeRangeField
    @Convert(converter = com.github.appreciated.vortex_crud.jpa.service.converter.DateTimeRangeConverter.class)
    private com.github.appreciated.vortex_crud.core.entity.DateTimeRange dateTimeRange;

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

    public com.github.appreciated.vortex_crud.core.entity.DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(com.github.appreciated.vortex_crud.core.entity.DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public com.github.appreciated.vortex_crud.core.entity.DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    public void setDateTimeRange(com.github.appreciated.vortex_crud.core.entity.DateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }
}
