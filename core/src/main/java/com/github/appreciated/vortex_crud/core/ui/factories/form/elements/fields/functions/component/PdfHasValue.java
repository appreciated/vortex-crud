package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.components.PdfDisplayComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class PdfHasValue extends CustomField<String> {

    private static final Logger log = LoggerFactory.getLogger(PdfHasValue.class);

    private final PdfDisplayComponent pdf;
    private final PdfDisplayComponent thumbnailPdf;
    private final Button btnPreview;
    private final Button btnDelete;
    private final Div overlay;
    private final Upload upload;
    private final Div card;
    private final Div thumbWrapper;
    private final VortexCrudResourceProvider resourceProvider;

    private String value;

    public PdfHasValue(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        // Hidden PDF element for resource registration
        pdf = new PdfDisplayComponent(resourceProvider);
        pdf.getStyle()
                .set("position", "absolute")
                .set("width", "0")
                .set("height", "0")
                .set("opacity", "0")
                .set("pointer-events", "none")
                .set("visibility", "hidden");

        // Thumbnail PDF for display
        thumbnailPdf = new PdfDisplayComponent(resourceProvider);
        thumbnailPdf.setSizeFull();
        thumbnailPdf.getStyle()
                .set("border-radius", "6px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("pointer-events", "none");

        btnPreview = new Button(VaadinIcon.EYE.create(), e -> openPreview());
        btnDelete = new Button(VaadinIcon.TRASH.create(), e -> clearPdf());
        btnPreview.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_PRIMARY);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_PRIMARY);

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

        thumbWrapper = new Div(thumbnailPdf, overlay);
        thumbWrapper.getStyle()
                .set("position", "relative")
                .set("width", "160px")
                .set("height", "200px")
                .set("overflow", "hidden")
                .set("border-radius", "6px")
                .set("box-shadow", "0 0 0 1px var(--lumo-contrast-10pct) inset");

        thumbWrapper.getElement().addEventListener("mouseenter",
                e -> overlay.getStyle().set("opacity", "1"));
        thumbWrapper.getElement().addEventListener("mouseleave",
                e -> overlay.getStyle().set("opacity", "0"));

        // Upload handler
        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setPdfFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(50 * 1024 * 1024);  // 50MB
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);
        upload.setAcceptedFileTypes("application/pdf", ".pdf");
        upload.addAllFinishedListener(e -> upload.clearFileList());
        upload.addFileRejectedListener(e -> Notification.show(e.getErrorMessage(), 2500, Notification.Position.BOTTOM_CENTER));
        upload.getElement().getStyle()
                .set("width", "160px")
                .set("height", "200px");

        Div uploadCard = new Div(upload);
        uploadCard.getStyle()
                .set("position", "relative")
                .set("width", "160px")
                .set("height", "200px")
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
                .set("cursor", "pointer");

        card = new Div(uploadCard, thumbWrapper);
        card.getStyle().set("display", "inline-block");

        // Add card and hidden PDF to component tree
        add(card, pdf);
        updateVisibility();
    }

    private void openPreview() {
        if (value == null || value.isBlank()) return;

        // Get PDF URL
        pdf.setPdfSource(value);
        String pdfSrc = pdf.getElement().getAttribute("src");

        getElement().executeJs("""
                    const iframe = document.createElement('iframe');
                    iframe.src = $0;
                    iframe.style.cssText = `
                      position:fixed; inset:0;
                      width:90vw; height:90vh;
                      margin:auto; background:white;
                      border:none;
                      z-index:9999;`;
                    const closeBtn = document.createElement('button');
                    closeBtn.textContent = '×';
                    closeBtn.style.cssText = `
                      position:fixed; top:20px; right:20px;
                      width:40px; height:40px;
                      border:none; border-radius:50%;
                      background:white; color:black;
                      font-size:30px; line-height:1;
                      cursor:pointer; z-index:10000;
                      box-shadow:0 2px 8px rgba(0,0,0,0.3);`;
                    const overlay = document.createElement('div');
                    overlay.style.cssText = `
                      position:fixed; inset:0;
                      background:rgba(0,0,0,0.85);
                      display:flex; align-items:center; justify-content:center;
                      z-index:9998;`;
                    overlay.appendChild(iframe);
                    overlay.appendChild(closeBtn);
                    closeBtn.addEventListener('click',()=>overlay.remove());
                    overlay.addEventListener('click',(e)=>{
                      if(e.target === overlay) overlay.remove();
                    });
                    document.body.appendChild(overlay);
                """, pdfSrc);
    }

    private void setPdfFromPath(String fullPath) {
        Path path = Path.of(fullPath);
        String fileName = path.getFileName().toString();
        log.info("PDF uploaded: {}", fileName);
        setValue(fileName);
    }

    private void clearPdf() {
        setValue(null);
    }

    private void loadThumbnail(String pdfFileName) {
        if (pdfFileName == null || pdfFileName.isBlank()) {
            thumbnailPdf.setVisible(false);
            return;
        }

        thumbnailPdf.setPdfSource(pdfFileName);
        thumbnailPdf.setVisible(true);
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
        log.info("setValue called with: {}", value);
        this.value = value;
        loadThumbnail(value);
        // Preload PDF resource for preview
        if (value != null && !value.isBlank()) {
            pdf.setPdfSource(value);
        }
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
        loadThumbnail(s);
        // Preload PDF resource for preview
        if (s != null && !s.isBlank()) {
            pdf.setPdfSource(s);
        }
        updateVisibility();
    }
}
