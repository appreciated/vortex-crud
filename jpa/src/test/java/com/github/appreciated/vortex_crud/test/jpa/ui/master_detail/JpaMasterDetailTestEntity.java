package com.github.appreciated.vortex_crud.test.jpa.ui.master_detail;


import com.github.appreciated.vortex_crud.jpa.service.annoations.NumericIdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "master_detail_tasks")
public class JpaMasterDetailTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Integer id;

    @TextField
    private String title;

    @TextField
    private String description;

    @TextField
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
