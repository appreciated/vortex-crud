package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;

public class ImageHasValue extends CustomField<String> {

    private final ImageDisplayComponent image;
    private final Button btnPreview;
    private final Button btnDelete;
    private final Div overlay;
    private final Upload upload;
    private final Div card;
    private final Div thumbWrapper;

    private final VortexCrudResourceProvider resourceProvider;
    private String value;

    public ImageHasValue(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        // === Picture card ===
        image = new ImageDisplayComponent(resourceProvider);
        image.setSizeFull();
        image.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "6px");

        btnPreview = new Button(VaadinIcon.EYE.create(), e -> openPreview(image));
        btnDelete = new Button(VaadinIcon.TRASH.create(), e -> clearImage());
        btnPreview.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);

        overlay = new Div(btnPreview, btnDelete);
        overlay.getStyle()
                .set("position", "absolute")
                .set("inset", "0")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("gap", "12px")
                .set("background", "rgba(0,0,0,0.45)")
                .set("opacity", "0")
                .set("transition", "opacity 120ms ease");

        thumbWrapper = new Div(image, overlay);
        thumbWrapper.getStyle()
                .set("position", "relative")
                .set("width", "120px")
                .set("height", "120px")
                .set("overflow", "hidden")
                .set("border-radius", "6px")
                .set("box-shadow", "0 0 0 1px var(--lumo-contrast-10pct) inset");

        thumbWrapper.getElement().addEventListener("mouseenter",
                e -> overlay.getStyle().set("opacity", "1"));
        thumbWrapper.getElement().addEventListener("mouseleave",
                e -> overlay.getStyle().set("opacity", "0"));

        // === Upload card ===
        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setImageFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(10 * 1024 * 1024);
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);
        upload.setAcceptedFileTypes("image/*");
        upload.addAllFinishedListener(e -> upload.clearFileList());
        upload.addFileRejectedListener(e -> Notification.show(e.getErrorMessage(), 2500, Notification.Position.BOTTOM_CENTER));
        upload.getElement().getStyle()
                .set("width", "120px")
                .set("height", "120px");

        Div plus = new Div(VaadinIcon.PLUS.create(), new Span("Upload"));
        plus.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("gap", "6px")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "12px");

        Div uploadCard = new Div(plus, upload);
        uploadCard.getStyle()
                .set("position", "relative")
                .set("width", "120px")
                .set("height", "120px")
                .set("border", "1px dashed var(--lumo-contrast-20pct)")
                .set("border-radius", "6px")
                .set("cursor", "pointer")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("background", "var(--lumo-base-color)");

        upload.getStyle()
                .set("position", "absolute")
                .set("inset", "0")
                .set("opacity", "0")
                .set("cursor", "pointer");

        card = new Div(uploadCard, thumbWrapper);
        card.getStyle().set("display", "inline-block");

        add(card);
        updateVisibility();
    }

    private void openPreview(ImageDisplayComponent image) {
        if (value == null || value.isBlank()) return;

        getElement().executeJs("""
                    const img = document.createElement('img');
                    img.src = $0;
                    img.style.cssText = `
                      position:fixed; inset:0;
                      max-width:90vw; max-height:90vh;
                      margin:auto; background:black;
                      z-index:9999; cursor:zoom-out;`;
                    const overlay = document.createElement('div');
                    overlay.style.cssText = `
                      position:fixed; inset:0;
                      background:rgba(0,0,0,0.85);
                      display:flex; align-items:center; justify-content:center;
                      z-index:9998;`;
                    overlay.appendChild(img);
                    overlay.addEventListener('click',()=>overlay.remove());
                    document.body.appendChild(overlay);
                """, image.getElement().getAttribute("src"));
    }

    private void setImageFromPath(String fileName) {
        setValue(fileName);
    }

    private void clearImage() {
        setValue(null);
    }

    private void updateVisibility() {
        boolean hasValue = value != null && !value.isBlank();
        thumbWrapper.setVisible(hasValue);
        overlay.getStyle().set("opacity", "0");
        card.getChildren().forEach(c -> c.setVisible(false));
        if (hasValue) {
            thumbWrapper.setVisible(true);
        } else {
            card.getComponentAt(0).setVisible(true);
        }
        upload.clearFileList();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        image.setImageSource(value);
        updateVisibility();
        super.setModelValue(value, true);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        btnDelete.setEnabled(!readOnly);
        btnPreview.setEnabled(!readOnly);
        upload.setEnabled(!readOnly);
        thumbWrapper.getStyle().set("pointer-events", readOnly ? "none" : "auto");
        overlay.setVisible(!readOnly);
    }

    @Override
    protected String generateModelValue() {
        return value;
    }

    @Override
    protected void setPresentationValue(String s) {
        this.value = s;
        image.setImageSource(s);
        updateVisibility();
    }
}
