package com.github.appreciated.vortex_crud.test.jpa.ui.master_details;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class JpaMasterDetailTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(IdFieldFactory.class)
    private Integer id;

    @Field(TextFieldFactory.class)
    private String title;

    @Field(TextFieldFactory.class)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
