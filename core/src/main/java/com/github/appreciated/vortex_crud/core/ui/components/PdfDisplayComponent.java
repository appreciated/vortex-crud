package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.streams.DownloadHandler;

@Tag("embed")
public class PdfDisplayComponent extends HtmlContainer {

    private final VortexCrudResourceProvider resourceProvider;

    public PdfDisplayComponent(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        getElement().setAttribute("type", "application/pdf");
    }

    public void setPdfSource(String src) {
        if (src != null) {
            DownloadHandler handler = resourceProvider.getResource(src);
            getElement().setAttribute("src", handler);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

}
