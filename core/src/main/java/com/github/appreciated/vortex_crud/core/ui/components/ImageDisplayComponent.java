package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.vaadin.flow.component.html.Image;

public class ImageDisplayComponent extends Image {

    private final VortexCrudResourceProvider resourceProvider;

    public ImageDisplayComponent(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public void setImageSource(String src) {
        if (src != null) {
            setSrc(resourceProvider.getResource(src));
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

}