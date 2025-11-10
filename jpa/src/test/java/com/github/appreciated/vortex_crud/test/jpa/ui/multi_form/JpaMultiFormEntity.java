package com.github.appreciated.vortex_crud.test.jpa.ui.multi_form;

import com.github.appreciated.vortex_crud.jpa.service.annoations.EmailField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.IntegerNumberField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "multi_form_test")
public class JpaMultiFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Information Form fields
    @NotBlank(message = "Profile name is required")
    @Column(name = "profile_name", nullable = false)
    @TextField
    private String profileName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false)
    @EmailField
    private String email;

    // Additional Details Form fields
    @Column(name = "description", length = 1000)
    @TextAreaField
    private String description;

    @Column(name = "age")
    @IntegerNumberField
    private Integer age;

    // Default constructor
    public JpaMultiFormEntity() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
