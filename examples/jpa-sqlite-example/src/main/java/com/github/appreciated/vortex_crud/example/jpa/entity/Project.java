package com.github.appreciated.vortex_crud.example.jpa.entity;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateTimePickerFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldRenderer;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JpaFieldRenderer(TextFieldFactory.class)
    private String name;

    @JpaFieldRenderer(TextFieldFactory.class)
    private String description;

    @JpaFieldRenderer(DateFieldFactory.class)
    private LocalDate startDate;

    @JpaFieldRenderer(DateFieldFactory.class)
    private LocalDate endDate;

    @JpaFieldRenderer(DateTimePickerFactory.class)
    private LocalDateTime createdAt;

    @JpaFieldRenderer(DateTimePickerFactory.class)
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
