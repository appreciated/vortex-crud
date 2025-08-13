package com.github.appreciated.vortex_crud.test.jpa.shared;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class JpaProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(IdFieldFactory.class)
    private Integer id;

    @Field(TextFieldFactory.class)
    private String name;

    @Field(TextFieldFactory.class)
    private String description;

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
}
