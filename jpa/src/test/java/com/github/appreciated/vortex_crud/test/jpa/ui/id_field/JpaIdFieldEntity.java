package com.github.appreciated.vortex_crud.test.jpa.ui.id_field;

import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "field_types_test")
public class JpaIdFieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Long id;

    @NotBlank
    @TextField
    @Column(name = "name")
    private String name;

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
}
