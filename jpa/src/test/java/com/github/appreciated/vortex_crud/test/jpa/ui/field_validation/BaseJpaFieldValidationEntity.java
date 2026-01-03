package com.github.appreciated.vortex_crud.test.jpa.ui.field_validation;

import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseJpaFieldValidationEntity {

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
}