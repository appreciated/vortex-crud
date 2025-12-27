package com.github.appreciated.vortex_crud.test.jpa.ui.kanban;


import com.github.appreciated.vortex_crud.jpa.service.annoations.IntegerNumberField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.NumericIdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.SelectField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "kanban_tasks")
public class JpaKanbanTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Integer id;

    @TextField
    private String title;

    @TextField
    private String description;

    @SelectField("enum-options")
    private String status;

    @Column(name = "row_index")
    @IntegerNumberField
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
