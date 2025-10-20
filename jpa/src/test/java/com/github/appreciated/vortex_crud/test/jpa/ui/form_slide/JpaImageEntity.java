package com.github.appreciated.vortex_crud.test.jpa.ui.form_slide;

import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.ImageField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "images")
public class JpaImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Integer id;

    @TextField
    @NotNull
    @Column(nullable = false)
    private String title;

    @ImageField(ImageResourceProvider.class)
    @Column
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
