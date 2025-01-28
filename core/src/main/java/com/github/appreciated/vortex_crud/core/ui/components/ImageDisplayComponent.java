package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProvider;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

public class ImageDisplayComponent extends Div {

    private final Image image;
    private final VortexCrudFileProvider vortexCrudFileProvider;

    public ImageDisplayComponent(VortexCrudFileProvider vortexCrudFileProvider) {
        this.vortexCrudFileProvider = vortexCrudFileProvider;
        image = new Image();
        image.setSizeFull();
        image.setHeight("150px");
        setObjectFit("cover");
        add(image);
        getStyle().set("overflow", "hidden");
    }

    public void setImageSource(String src) {
        if (src != null) {
            image.setSrc(vortexCrudFileProvider.getResource(src));
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }

    public void setObjectFit(String cover) {
        image.getStyle().set("object-fit", cover);
    }
}