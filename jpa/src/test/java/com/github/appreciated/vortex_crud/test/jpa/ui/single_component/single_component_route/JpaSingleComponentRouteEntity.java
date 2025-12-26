package com.github.appreciated.vortex_crud.test.jpa.ui.single_component.single_component_route;

import com.github.appreciated.vortex_crud.jpa.service.annoations.MarkDownField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "single_form_route_test")
public class JpaSingleComponentRouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @TextField
    @Column(name = "name")
    private String name;

    @Column(name = "notes", columnDefinition = "TEXT")
    @MarkDownField
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
