package com.github.appreciated.vortex_crud.test.jpa.ui.file;

import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.FileField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "generic_files")
public class JpaFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Integer id;

    @TextField
    private String name;

    @FileField(LocalFileResourceProvider.class)
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
