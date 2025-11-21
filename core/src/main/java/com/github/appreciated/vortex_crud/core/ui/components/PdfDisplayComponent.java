package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.server.streams.AbstractDownloadHandler;
import com.vaadin.flow.server.streams.DownloadHandler;

@Tag("embed")
public class PdfDisplayComponent extends HtmlContainer {

    private static final String SRC_ATTRIBUTE = "src";
    private static final String TYPE_ATTRIBUTE = "type";
    private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors
            .attributeWithDefault(SRC_ATTRIBUTE, "");

    private final VortexCrudResourceProvider resourceProvider;

    public PdfDisplayComponent(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        getElement().setAttribute(TYPE_ATTRIBUTE, "application/pdf");
        setWidth("100%");
        setHeight("100%");
    }

    public void setPdfSource(String src) {
        if (src != null && !src.isEmpty()) {
            DownloadHandler downloadHandler = resourceProvider.getResource(src);
            if (downloadHandler instanceof AbstractDownloadHandler<?> handler) {
                handler.inline();
            }
            getElement().setAttribute(SRC_ATTRIBUTE, downloadHandler);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public String getSrc() {
        return get(srcDescriptor);
    }
}
