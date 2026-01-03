package com.github.appreciated.vortex_crud.test.jpa.ui.additional_fields.password;

import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "password_test")
@Getter
@Setter
@NoArgsConstructor
public class JpaPasswordTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    @TextField
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @TextAreaField
    private String description;

    @Column(name = "password")
    @PasswordField
    private String password;

    @Column(name = "price", precision = 10, scale = 2)
    @BigDecimalNumberField
    private BigDecimal price;

    @Column(name = "video_url")
    @VideoField(LocalVideoResourceProvider.class)
    private String videoUrl;
}