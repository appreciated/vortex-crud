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

import java.nio.file.Path;

public class PdfHasValue extends CustomField<String> {

    private final PdfDisplayComponent pdfThumbnail;
    private final PdfDisplayComponent pdfFull;
    private final Button btnPreview;
    private final Button btnDelete;
    private final Div overlay;
    private final Upload upload;
    private final Div card;
    private final Div thumbWrapper;

    private String value;

    public PdfHasValue(VortexCrudResourceProvider resourceProvider) {

        // === PDF thumbnail card ===
        pdfThumbnail = new PdfDisplayComponent(resourceProvider);
        pdfThumbnail.setSizeFull();
        pdfThumbnail.getStyle()
                .set("border-radius", "6px")
                .set("pointer-events", "none");

        // Keep a full PDF component in DOM for preview (hidden)
        pdfFull = new PdfDisplayComponent(resourceProvider);
        pdfFull.setVisible(false);

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

        thumbWrapper = new Div(pdfThumbnail, overlay);
        thumbWrapper.getStyle()
                .set("position", "relative")
                .set("width", "160px")
                .set("height", "200px")
                .set("overflow", "hidden")
                .set("border-radius", "6px")
                .set("box-shadow", "0 0 0 1px var(--lumo-contrast-10pct) inset")
                .set("background", "var(--lumo-contrast-5pct)");

        thumbWrapper.getElement().addEventListener("mouseenter",
                e -> overlay.getStyle().set("opacity", "1"));
        thumbWrapper.getElement().addEventListener("mouseleave",
                e -> overlay.getStyle().set("opacity", "0"));

        // === Upload card ===
        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setPdfFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(50 * 1024 * 1024); // 50MB for PDFs
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

        card = new Div(uploadCard, thumbWrapper, pdfFull);
        card.getStyle().set("display", "inline-block");

        add(card);
        updateVisibility();
    }

    private void openPreview() {
        if (value == null || value.isBlank()) return;

        // Get the PDF URL from the full PDF component
        getElement().executeJs("""
                    const embed = $0;
                    const src = embed.getAttribute('src');

                    const iframe = document.createElement('iframe');
                    iframe.src = src;
                    iframe.style.cssText = `
                      position:fixed; inset:0;
                      width:100vw; height:100vh;
                      border:none; background:white;
                      z-index:9999;`;

                    const overlay = document.createElement('div');
                    overlay.style.cssText = `
                      position:fixed; inset:0;
                      background:rgba(0,0,0,0.85);
                      display:flex; align-items:center; justify-content:center;
                      z-index:9998;`;

                    const closeBtn = document.createElement('button');
                    closeBtn.textContent = '×';
                    closeBtn.style.cssText = `
                      position:fixed; top:20px; right:20px;
                      width:48px; height:48px;
                      border:none; border-radius:24px;
                      background:rgba(255,255,255,0.2); color:white;
                      font-size:32px; cursor:pointer;
                      z-index:10000; backdrop-filter:blur(4px);
                      transition:background 200ms;`;
                    closeBtn.onmouseover = () => closeBtn.style.background = 'rgba(255,255,255,0.3)';
                    closeBtn.onmouseout = () => closeBtn.style.background = 'rgba(255,255,255,0.2)';
                    closeBtn.onclick = () => overlay.remove();

                    overlay.appendChild(iframe);
                    document.body.appendChild(overlay);
                    document.body.appendChild(closeBtn);

                    overlay.addEventListener('click', (e) => {
                      if (e.target === overlay) {
                        overlay.remove();
                        closeBtn.remove();
                      }
                    });
                """, pdfFull.getElement());
    }

    private void setPdfFromPath(String fullPath) {
        Path path = Path.of(fullPath);
        String fileName = path.getFileName().toString();
        setValue(fileName);
    }

    private void clearPdf() {
        setValue(null);
    }

    private void updateVisibility() {
        boolean hasValue = value != null && !value.isBlank();
        thumbWrapper.setVisible(hasValue);
        overlay.getStyle().set("opacity", "0");
        card.getChildren().forEach(c -> c.setVisible(false));
        if (hasValue) {
            thumbWrapper.setVisible(true);
            pdfFull.setVisible(true); // Keep in DOM for preview
        } else {
            card.getComponentAt(0).setVisible(true);
        }
        upload.clearFileList();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        pdfThumbnail.setPdfSource(value);
        pdfFull.setPdfSource(value);
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
        pdfThumbnail.setPdfSource(s);
        pdfFull.setPdfSource(s);
        updateVisibility();
    }
}
