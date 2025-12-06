package com.github.appreciated.vortex_crud.test.jpa.ui.field_types;

import com.github.appreciated.vortex_crud.core.entity.DateRange;
import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import com.github.appreciated.vortex_crud.test.jpa.ui.field_types.converters.DateRangeConverter;
import com.github.appreciated.vortex_crud.test.jpa.ui.field_types.converters.DateTimeRangeConverter;
import com.github.appreciated.vortex_crud.test.jpa.ui.field_types.converters.StringSetConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "field_types_test")
public class JpaFieldTypesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    @TextField
    private String name;

    @Column(name = "tags")
    @Convert(converter = StringSetConverter.class)
    @MultiSelectValueField("tags")
    private Set<String> tags;

    @Column(name = "pdf_doc")
    @PdfField(LocalPdfResourceProvider.class)
    private String pdfDoc;

    @Column(name = "notes", columnDefinition = "TEXT")
    @TextAreaField
    private String notes;

    @Column(name = "date_range", length = 500)
    @Convert(converter = DateRangeConverter.class)
    @DateRangeField
    private DateRange dateRange;

    @Column(name = "date_time_range", length = 500)
    @Convert(converter = DateTimeRangeConverter.class)
    @DateTimeRangeField
    private DateTimeRange dateTimeRange;

    @Column(name = "checkbox_field")
    @CheckboxField
    private Boolean checkboxField;

    @Column(name = "date_field")
    @DateField
    private LocalDate dateField;

    @Column(name = "datetime_field")
    @DateTimePickerField
    private LocalDateTime dateTimeField;

    @Column(name = "double_field")
    @DoubleNumberField
    private Double doubleField;

    @Column(name = "email_field")
    @EmailField
    private String emailField;

    @Column(name = "file_field")
    @FileField(LocalFileResourceProvider.class)
    private String fileField;

    @Column(name = "image_field")
    @ImageField(LocalImageResourceProvider.class)
    private String imageField;

    @Column(name = "integer_field")
    @IntegerNumberField
    private Integer integerField;

    @Column(name = "select_field")
    @SelectField("selectField")
    private String selectField;

    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
    public String getPdfDoc() { return pdfDoc; }
    public void setPdfDoc(String pdfDoc) { this.pdfDoc = pdfDoc; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public DateRange getDateRange() { return dateRange; }
    public void setDateRange(DateRange dateRange) { this.dateRange = dateRange; }
    public DateTimeRange getDateTimeRange() { return dateTimeRange; }
    public void setDateTimeRange(DateTimeRange dateTimeRange) { this.dateTimeRange = dateTimeRange; }

    public Boolean getCheckboxField() { return checkboxField; }
    public void setCheckboxField(Boolean checkboxField) { this.checkboxField = checkboxField; }
    public LocalDate getDateField() { return dateField; }
    public void setDateField(LocalDate dateField) { this.dateField = dateField; }
    public LocalDateTime getDateTimeField() { return dateTimeField; }
    public void setDateTimeField(LocalDateTime dateTimeField) { this.dateTimeField = dateTimeField; }
    public Double getDoubleField() { return doubleField; }
    public void setDoubleField(Double doubleField) { this.doubleField = doubleField; }
    public String getEmailField() { return emailField; }
    public void setEmailField(String emailField) { this.emailField = emailField; }
    public String getFileField() { return fileField; }
    public void setFileField(String fileField) { this.fileField = fileField; }
    public String getImageField() { return imageField; }
    public void setImageField(String imageField) { this.imageField = imageField; }
    public Integer getIntegerField() { return integerField; }
    public void setIntegerField(Integer integerField) { this.integerField = integerField; }
    public String getSelectField() { return selectField; }
    public void setSelectField(String selectField) { this.selectField = selectField; }
}
