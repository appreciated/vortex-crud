package com.github.appreciated.vortex_crud.test.jpa.ui.field_types;

import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.entity.DateRange;
import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import com.github.appreciated.vortex_crud.jpa.service.annoations.DateRangeField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.DateTimeRangeField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.MultiSelectValueField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.PdfField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import com.github.appreciated.vortex_crud.jpa.service.converter.DateRangeConverter;
import com.github.appreciated.vortex_crud.jpa.service.converter.DateTimeRangeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "missing_features_test")
public class JpaFieldTypesEntity {
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

    @Convert(converter = DateRangeConverter.class)
    @Column(name = "date_range", columnDefinition = "TEXT")
    @DateRangeField
    private DateRange dateRange;

    @Convert(converter = DateTimeRangeConverter.class)
    @Column(name = "date_time_range", columnDefinition = "TEXT")
    @DateTimeRangeField
    private DateTimeRange dateTimeRange;

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

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public DateTimeRange getDateTimeRange() {
        return dateTimeRange;
    }

    public void setDateTimeRange(DateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }
}
