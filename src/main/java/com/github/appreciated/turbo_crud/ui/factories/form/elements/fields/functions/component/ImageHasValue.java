package com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.HasAriaLabel;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.shared.Registration;

public class ImageHasValue extends Div implements HasValue<HasValue.ValueChangeEvent<String>, String>, HasLabel, HasAriaLabel {

    private final ImageDisplayComponent image;
    private final TextField imageField;
    private final Button cancelButton;
    private final Upload upload;
    private final FileBuffer buffer;

    private String value;

    public ImageHasValue(TurboCrudFileProvider turboCrudFileProvider) {
        // Initialize the image component as a thumbnail
        image = new ImageDisplayComponent(turboCrudFileProvider);
        image.setWidth("30px");
        image.setHeight("30px");

        // Initialize the file name text field
        imageField = new TextField();
        imageField.setPlaceholder("Select or upload an image...");
        imageField.setReadOnly(true);
        imageField.setWidthFull();

        // Set the image as the prefix component
        imageField.setPrefixComponent(image);

        // Initialize the cancel button to clear the image selection
        cancelButton = new Button(VaadinIcon.TRASH.create(), event -> clearImage());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Set the cancel button as the suffix component
        imageField.setSuffixComponent(cancelButton);

        // Configure the upload component
        buffer = new FileBuffer(fileName -> turboCrudFileProvider.getPathForFile(fileName).toFile());
        upload = new Upload(buffer);
        upload.setMaxFileSize(10000000);
        upload.addSucceededListener(event -> setImageFromStream(turboCrudFileProvider.getPathForFile(event.getFileName()).toString()));
        upload.getStyle().set("padding", "unset");

        // Arrange components
        add(imageField, upload);
        updateVisibility();
        getStyle().set("overflow", "hidden");
    }

    public ImageHasValue(String path, TurboCrudFileProvider turboCrudFileProvider) {
        this(turboCrudFileProvider);
        setValue(path);
    }

    private void setImageFromStream(String fileName) {
        image.setImageSource(fileName);
        imageField.setValue(fileName);
        setValue(fileName);
    }

    private void clearImage() {
        setValue(null);
    }

    private void updateVisibility() {
        boolean hasValue = value != null;
        imageField.setVisible(hasValue);
        upload.getStyle().set("opacity", hasValue ? "0" : "1");
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        image.setImageSource(value);
        imageField.setValue(value != null ? value : "");
        updateVisibility();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<String>> listener) {
        return null; // Implement this if required
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        cancelButton.setEnabled(!readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        // Optional implementation if needed
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false; // Implement if necessary
    }
}
