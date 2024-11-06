package com.github.appreciated.turbo_crud.ui.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageDisplayComponent extends Div {

    private final Image image;

    public ImageDisplayComponent() {
        image = new Image();
        add(image);
        getStyle().set("overflow", "hidden");
    }

    public void setImageSource(String src) {
        if (src != null) {
            image.setSrc(new StreamResource(src.substring(src.lastIndexOf("/") + 1), () -> {
                try {
                    return new FileInputStream(src);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }));
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }
}