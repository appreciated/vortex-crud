package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;

import java.nio.file.Path;

public class FileHasValue extends CustomField<String> {

    private final VortexCrudResourceProvider resourceProvider;
    private final Button btnDownload;
    private final Button btnDelete;
    private final Span fileNameLabel;
    private final Div fileInfoWrapper;
    private final Upload upload;
    private final Div card;

    private String value;

    public FileHasValue(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        // === File info card ===
        fileNameLabel = new Span();
        fileNameLabel.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis")
                .set("white-space", "nowrap")
                .set("max-width", "200px");

        btnDownload = new Button(VaadinIcon.DOWNLOAD.create(), e -> downloadFile());
        btnDelete = new Button(VaadinIcon.TRASH.create(), e -> clearFile());
        btnDownload.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        Div buttonGroup = new Div(btnDownload, btnDelete);
        buttonGroup.getStyle()
                .set("display", "flex")
                .set("gap", "4px");

        fileInfoWrapper = new Div(fileNameLabel, buttonGroup);
        fileInfoWrapper.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "12px")
                .set("padding", "8px 12px")
                .set("border-radius", "6px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("box-shadow", "0 0 0 1px var(--lumo-contrast-10pct) inset");

        // === Upload card ===
        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setFileFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(100 * 1024 * 1024); // 100MB for generic files
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);
        upload.addAllFinishedListener(e -> upload.clearFileList());
        upload.addFileRejectedListener(e -> Notification.show(e.getErrorMessage(), 2500, Notification.Position.BOTTOM_CENTER));

        Div uploadCard = new Div(upload);
        uploadCard.getStyle()
                .set("border", "1px dashed var(--lumo-contrast-20pct)")
                .set("border-radius", "6px")
                .set("padding", "12px")
                .set("background", "var(--lumo-base-color)");

        card = new Div(uploadCard, fileInfoWrapper);

        add(card);
        updateVisibility();
    }

    private void downloadFile() {
        if (value == null || value.isBlank()) return;

        // Create anchor for download
        Anchor downloadAnchor = new Anchor(resourceProvider.getResource(value), "");
        downloadAnchor.getElement().setAttribute("download", value);
        downloadAnchor.getElement().callJsFunction("click");
    }

    private void setFileFromPath(String fullPath) {
        Path path = Path.of(fullPath);
        String fileName = path.getFileName().toString();
        setValue(fileName);
    }

    private void clearFile() {
        setValue(null);
    }

    private void updateVisibility() {
        boolean hasValue = value != null && !value.isBlank();
        fileInfoWrapper.setVisible(hasValue);
        card.getChildren().forEach(c -> c.setVisible(false));
        if (hasValue) {
            fileNameLabel.setText(value);
            fileInfoWrapper.setVisible(true);
        } else {
            card.getComponentAt(0).setVisible(true);
        }
        upload.clearFileList();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
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
        btnDownload.setEnabled(!readOnly);
        upload.setEnabled(!readOnly);
    }

    @Override
    protected String generateModelValue() {
        return value;
    }

    @Override
    protected void setPresentationValue(String s) {
        this.value = s;
        updateVisibility();
    }
}
