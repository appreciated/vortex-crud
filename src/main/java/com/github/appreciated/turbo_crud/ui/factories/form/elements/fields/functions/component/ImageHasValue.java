package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageHasValue extends Div implements HasValue<HasValue.ValueChangeEvent<String>, String> {

    private final Image image;

    public ImageHasValue() {
        this(null, null);
    }

    public ImageHasValue(String src, String alt) {
        image = new Image();
        setValue(src);
        image.setAlt(alt);
        setValue(src);
        add(image);
    }

    @Override
    public void setValue(String value) {
        if (value != null) {
            image.setSrc(new StreamResource(value.substring(value.lastIndexOf("/") + 1), () -> {
                try {
                    return new FileInputStream(value);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }));
        } else {
            image.setAlt(null);
        }

    }

    @Override
    public String getValue() {
        return image.getSrc();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<String>> listener) {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {

    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }
}