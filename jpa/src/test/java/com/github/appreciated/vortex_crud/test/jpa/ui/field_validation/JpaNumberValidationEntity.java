package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "number_validation_test")
public class JpaNumberValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field is required")
    @Column(name = "required_field", nullable = false)
    @TextField
    private String requiredField;

    @Email(message = "Please enter a valid email address")
    @Column(name = "email_field")
    @EmailField
    private String emailField;

    @Min(value = 1, message = "Value must be greater than 0")
    @Column(name = "numeric_field")
    @DoubleNumberField
    private Double numericField;

    @Column(name = "date_field")
    @DateField
    private LocalDate dateField;

    @Column(name = "datetime_field")
    @DateTimePickerField
    private LocalDateTime dateTimeField;

    @Enumerated(EnumType.STRING)
    @Column(name = "enum_field")
    @SelectField(value = "enum-options")
    private JpaFieldValidationEnum enumField;

    @Column(name = "image_field")
    @ImageField(LocalImageResourceProvider.class)
    private String imageField;

    @Column(name = "checkbox_field")
    @CheckboxField
    private Boolean checkboxField;

    public JpaNumberValidationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    public String getEmailField() {
        return emailField;
    }

    public void setEmailField(String emailField) {
        this.emailField = emailField;
    }

    public Double getNumericField() {
        return numericField;
    }

    public void setNumericField(Double numericField) {
        this.numericField = numericField;
    }

    public LocalDate getDateField() {
        return dateField;
    }

    public void setDateField(LocalDate dateField) {
        this.dateField = dateField;
    }

    public LocalDateTime getDateTimeField() {
        return dateTimeField;
    }

    public void setDateTimeField(LocalDateTime dateTimeField) {
        this.dateTimeField = dateTimeField;
    }

    public JpaFieldValidationEnum getEnumField() {
        return enumField;
    }

    public void setEnumField(JpaFieldValidationEnum enumField) {
        this.enumField = enumField;
    }

    public String getImageField() {
        return imageField;
    }

    public void setImageField(String imageField) {
        this.imageField = imageField;
    }

    public Boolean getCheckboxField() {
        return checkboxField;
    }

    public void setCheckboxField(Boolean checkboxField) {
        this.checkboxField = checkboxField;
    }
}
