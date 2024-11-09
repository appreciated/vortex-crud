package com.github.appreciated.turbo_crud.ui.components;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

public class ImageDisplayComponent extends Div {

    private final Image image;
    private final TurboCrudFileProvider turboCrudFileProvider;

    public ImageDisplayComponent(TurboCrudFileProvider turboCrudFileProvider) {
        this.turboCrudFileProvider = turboCrudFileProvider;
        image = new Image();
        image.setSizeFull();
        image.getStyle().set("object-fit", "contain");
        add(image);
        getStyle().set("overflow", "hidden");
    }

    public void setImageSource(String src) {
        if (src != null) {
            image.setSrc(turboCrudFileProvider.getResource(src));
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }
}