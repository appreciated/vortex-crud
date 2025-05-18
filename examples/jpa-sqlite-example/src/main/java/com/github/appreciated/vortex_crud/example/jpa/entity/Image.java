package com.github.appreciated.vortex_crud.example.jpa.entity;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldRenderer;
import jakarta.persistence.*;
import org.springframework.data.annotation.ReadOnlyProperty;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JpaFieldRenderer(TextFieldFactory.class)
    private String title;

    @JpaFieldRenderer(TextFieldFactory.class)
    private String url;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}