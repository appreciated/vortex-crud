package com.github.appreciated.vortex_crud.test.jpa.ui.pdf;

import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.annoations.IdField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.PdfField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

@Entity
@Table(name = "pdf_documents")
public class JpaPdfEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @IdField
    private Integer id;

    @TextField
    private String title;

    @PdfField(LocalPdfResourceProvider.class)
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
