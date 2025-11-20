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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class FileHasValue extends CustomField<String> {

    private static final Logger log = LoggerFactory.getLogger(FileHasValue.class);

    private final VortexCrudResourceProvider resourceProvider;
    private final Span filenameLabel;
    private final Anchor downloadLink;
    private final Button btnDelete;
    private final Upload upload;
    private final Div card;
    private final Div fileDisplay;

    private String value;

    public FileHasValue(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        // Filename display
        filenameLabel = new Span();
        filenameLabel.getStyle()
                .set("font-weight", "500")
                .set("color", "var(--lumo-body-text-color)");

        // Download link/button
        downloadLink = new Anchor();
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.setTarget("_blank");

        Button btnDownload = new Button(VaadinIcon.DOWNLOAD.create());
        btnDownload.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
        downloadLink.add(btnDownload);
        downloadLink.getStyle()
                .set("text-decoration", "none");

        btnDelete = new Button(VaadinIcon.TRASH.create(), e -> clearFile());
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        Div buttonContainer = new Div(downloadLink, btnDelete);
        buttonContainer.getStyle()
                .set("display", "flex")
                .set("gap", "8px")
                .set("align-items", "center");

        fileDisplay = new Div(filenameLabel, buttonContainer);
        fileDisplay.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "space-between")
                .set("gap", "16px")
                .set("padding", "12px 16px")
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "6px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("min-width", "300px");

        // Upload handler
        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setFileFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(100 * 1024 * 1024);  // 100MB
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);
        upload.addAllFinishedListener(e -> upload.clearFileList());
        upload.addFileRejectedListener(e -> Notification.show(e.getErrorMessage(), 2500, Notification.Position.BOTTOM_CENTER));

        Div uploadCard = new Div(upload);
        uploadCard.getStyle()
                .set("border", "1px dashed var(--lumo-contrast-20pct)")
                .set("border-radius", "6px")
                .set("padding", "24px")
                .set("text-align", "center")
                .set("background", "var(--lumo-base-color)")
                .set("min-width", "300px");

        card = new Div(uploadCard, fileDisplay);
        card.getStyle().set("display", "inline-block");

        add(card);
        updateVisibility();
    }

    private void setFileFromPath(String fullPath) {
        Path path = Path.of(fullPath);
        String fileName = path.getFileName().toString();
        log.info("File uploaded: {}", fileName);
        setValue(fileName);
    }

    private void clearFile() {
        setValue(null);
    }

    private void updateFileDisplay(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            fileDisplay.setVisible(false);
            return;
        }

        filenameLabel.setText(fileName);
        downloadLink.setHref(resourceProvider.getResource(fileName));
        fileDisplay.setVisible(true);
    }

    private void updateVisibility() {
        boolean hasValue = value != null && !value.isBlank();
        fileDisplay.setVisible(hasValue);
        card.getChildren().forEach(c -> c.setVisible(false));
        if (hasValue) {
            fileDisplay.setVisible(true);
        } else {
            card.getComponentAt(0).setVisible(true);
        }
        upload.clearFileList();
    }

    @Override
    public void setValue(String value) {
        log.info("setValue called with: {}", value);
        this.value = value;
        updateFileDisplay(value);
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
        upload.setEnabled(!readOnly);
        btnDelete.setVisible(!readOnly);
    }

    @Override
    protected String generateModelValue() {
        return value;
    }

    @Override
    protected void setPresentationValue(String s) {
        this.value = s;
        updateFileDisplay(s);
        updateVisibility();
    }
}
