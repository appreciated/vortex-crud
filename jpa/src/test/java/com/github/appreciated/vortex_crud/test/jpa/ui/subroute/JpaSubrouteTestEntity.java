package com.github.appreciated.vortex_crud.test.jpa.ui.subroute;

import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "subroute_tasks")
public class JpaSubrouteTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Integer id;

    @TextField
    private String title;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
