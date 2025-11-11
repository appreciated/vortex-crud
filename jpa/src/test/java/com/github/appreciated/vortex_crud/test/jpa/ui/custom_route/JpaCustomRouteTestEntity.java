package com.github.appreciated.vortex_crud.test.jpa.ui.custom_route;

import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "custom_route_items")
public class JpaCustomRouteTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Integer id;

    @TextField
    private String name;

    @TextField
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
