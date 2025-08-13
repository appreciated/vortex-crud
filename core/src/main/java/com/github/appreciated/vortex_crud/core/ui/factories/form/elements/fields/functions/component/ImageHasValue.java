package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;

import static com.vaadin.flow.component.button.ButtonVariant.*;

public class ImageHasValue extends CustomField<String> {

    private final ImageDisplayComponent image;
    private final Button deleteButton;
    private final Upload upload;
    private final Div imageWrapper;

    private String value;

    public ImageHasValue(VortexCrudResourceProvider resourceProvider) {
        image = new ImageDisplayComponent(resourceProvider);
        image.setSizeFull();
        image.getStyle().set("border-radius", "3px");

        deleteButton = new Button(VaadinIcon.TRASH.create(), event -> clearImage());
        deleteButton.addThemeVariants(LUMO_ERROR, LUMO_PRIMARY, LUMO_LARGE);
        deleteButton.getStyle().set("position", "absolute")
                .set("top", "50%")
                .set("left", "50%")
                .set("border-radius", "100%")
                .set("padding", "unset !important")
                .set("transform", "translate(-50%, -50%)")
                .set("visibility", "hidden");

        imageWrapper = new Div(image, deleteButton);
        imageWrapper.getStyle().set("position", "relative")
                .set("width", "100%")
                .set("height", "100%")
                .set("overflow", "hidden");

        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setImageFromStream(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setSizeFull();
        upload.setMaxFiles(1);
        upload.setMaxFileSize(10000000);

        Div container = new Div(imageWrapper, upload);
        container.getStyle().set("overflow", "hidden")
                .set("position", "relative")
                .set("width", "100%")
                .set("height", "200px");

        add(container);
        updateVisibility();

        imageWrapper.getStyle().set("cursor", "pointer");
        imageWrapper.addClickListener(
                e -> deleteButton.getStyle().set("visibility", deleteButton.getStyle().get("visibility").equals("visible") ? "hidden" : "visible")
        );
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
        imageWrapper.getStyle().set("display", !hasValue ? "none" : "block");
        deleteButton.getStyle().set("visibility", "hidden");
        upload.getStyle().set("display", hasValue ? "none" : "block");
        upload.getStyle().set("opacity", hasValue ? "0" : "1");
        upload.clearFileList();
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
        deleteButton.setEnabled(!readOnly);
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

