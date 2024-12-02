package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;

public class ImageHasValue extends CustomField<String> {

    private final ImageDisplayComponent image;
    private final Button cancelButton;
    private final Upload upload;
    private final FileBuffer buffer;

    private String value;

    public ImageHasValue(TurboCrudFileProvider turboCrudFileProvider) {
        // Initialize the image component as a thumbnail
        image = new ImageDisplayComponent(turboCrudFileProvider);
        image.setWidth("200px");
        image.setHeight("200px");
        image.getStyle().set("border-radius", "3px");

        // Initialize the cancel button to clear the image selection
        cancelButton = new Button(VaadinIcon.TRASH.create(), event -> clearImage());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Configure the upload component
        buffer = new FileBuffer(fileName -> turboCrudFileProvider.getPathForFile(fileName).toFile());
        upload = new Upload(buffer);
        upload.setMaxFileSize(10000000);
        upload.addSucceededListener(event -> setImageFromStream(turboCrudFileProvider.getPathForFile(event.getFileName()).toString()));
        upload.getStyle().set("padding", "unset");

        // Arrange components
        add(new Div(image, upload));
        updateVisibility();
        getStyle().set("overflow", "hidden");
    }

    public ImageHasValue(String path, TurboCrudFileProvider turboCrudFileProvider) {
        this(turboCrudFileProvider);
        setValue(path);
    }

    private void setImageFromStream(String fileName) {
        image.setImageSource(fileName);
        setValue(fileName);
    }

    private void clearImage() {
        setValue(null);
    }

    private void updateVisibility() {
        boolean hasValue = value != null;
        image.setVisible(hasValue);
        upload.getStyle().set("display", hasValue ? "none" : "block");
        upload.getStyle().set("opacity", hasValue ? "0" : "1");
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        image.setImageSource(value);
        updateVisibility();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        cancelButton.setEnabled(!readOnly);
    }

    @Override
    protected String generateModelValue() {
        return "";
    }

    @Override
    protected void setPresentationValue(String s) {
        image.setImageSource(s);
    }
}
