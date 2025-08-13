package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EmailFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import com.github.appreciated.vortex_crud.jpa.service.ImageFieldConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "jpa_validation_test")
public class JpaFieldValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "This field is required")
    @Column(name = "required_field", nullable = false)
    @Field(TextFieldFactory.class)
    private String requiredField;

    @Email(message = "Please enter a valid email address")
    @Column(name = "email_field")
    @Field(EmailFieldFactory.class)
    private String emailField;

    @Min(value = 1, message = "Value must be greater than 0")
    @Column(name = "numeric_field")
    @Field(DoubleNumberFieldFactory.class)
    private Double numericField;

    @Column(name = "date_field")
    @Field(DateFieldFactory.class)
    private LocalDate dateField;

    @Enumerated(EnumType.STRING)
    @Column(name = "enum_field")
    @Field(SelectFieldFactory.class)
    @SelectValues("enum-options")
    private JpaFieldValidationEnum enumField;

    @Column(name = "image_field")
    @Field(ImageFieldFactory.class)
    @ImageFieldConfiguration(ImageResourceProvider.class)
    private String imageField;

    // Default constructor
    public JpaFieldValidationEntity() {
    }

    // Getters and setters
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
}