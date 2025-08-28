package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import jakarta.persistence.*;

@Entity
@Table(name = "kanban_tasks")
public class JpaKanbanTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(IdFieldFactory.class)
    private Integer id;

    @Field(TextFieldFactory.class)
    private String title;

    @Field(SelectFieldFactory.class)
    @SelectValues("enum-options")
    private String status;

    @Column(name = "row_index")
    private Integer rowIndex;

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

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }
}
