package com.github.appreciated.vortex_crud.test.jpa.ui.grid;

import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.ImageField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.NumericIdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "grid_images")
public class JpaImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NumericIdField
    private Integer id;

    @TextField
    private String title;

    @ImageField(LocalImageResourceProvider.class)
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
