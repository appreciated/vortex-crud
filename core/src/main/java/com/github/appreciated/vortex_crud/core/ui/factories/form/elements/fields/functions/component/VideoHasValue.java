package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.components.VideoDisplayComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.streams.UploadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VideoHasValue extends CustomField<String> {

    private static final Logger log = LoggerFactory.getLogger(VideoHasValue.class);

    private final VortexCrudResourceProvider resourceProvider;
    private final VideoDisplayComponent video;
    private final Image thumbnail;
    private final Button btnPreview;
    private final Button btnDelete;
    private final Div overlay;
    private final Upload upload;
    private final Div card;
    private final Div thumbWrapper;

    private String value;

    public VideoHasValue(VortexCrudResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;

        // Video element (hidden in DOM but attached to register resources)
        // We keep it in the DOM with CSS hiding instead of setVisible(false)
        // so that StreamResource URLs get properly registered
        video = new VideoDisplayComponent(resourceProvider);
        video.getStyle()
                .set("position", "absolute")
                .set("width", "0")
                .set("height", "0")
                .set("opacity", "0")
                .set("pointer-events", "none")
                .set("visibility", "hidden");

        // Thumbnail image for display
        thumbnail = new Image();
        thumbnail.setSizeFull();
        thumbnail.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "6px")
                .set("background", "var(--lumo-contrast-5pct)");

        btnPreview = new Button(VaadinIcon.PLAY.create(), e -> openPreview());
        btnDelete = new Button(VaadinIcon.TRASH.create(), e -> clearVideo());
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

        thumbWrapper = new Div(thumbnail, overlay);
        thumbWrapper.getStyle()
                .set("position", "relative")
                .set("width", "160px")
                .set("height", "90px")
                .set("overflow", "hidden")
                .set("border-radius", "6px")
                .set("box-shadow", "0 0 0 1px var(--lumo-contrast-10pct) inset");

        thumbWrapper.getElement().addEventListener("mouseenter",
                e -> overlay.getStyle().set("opacity", "1"));
        thumbWrapper.getElement().addEventListener("mouseleave",
                e -> overlay.getStyle().set("opacity", "0"));

        UploadHandler handler = UploadHandler.toFile(
                (metadata, file) -> setVideoFromPath(file.getPath()),
                metadata -> resourceProvider.getPathForFile(metadata.fileName()).toFile()
        );
        upload = new Upload(handler);
        upload.setMaxFiles(1);
        upload.setMaxFileSize(100 * 1024 * 1024);
        upload.setAutoUpload(true);
        upload.setDropAllowed(true);
        upload.setAcceptedFileTypes("video/*");
        upload.addAllFinishedListener(e -> upload.clearFileList());
        upload.addFileRejectedListener(e -> Notification.show(e.getErrorMessage(), 2500, Notification.Position.BOTTOM_CENTER));
        upload.getElement().getStyle()
                .set("width", "160px")
                .set("height", "90px");

        Div uploadCard = new Div(upload);
        uploadCard.getStyle()
                .set("position", "relative")
                .set("width", "160px")
                .set("height", "90px")
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

        // Add both card and hidden video to component tree
        // Video must be in DOM for StreamResource registration to work
        add(card, video);
        updateVisibility();
    }

    private void openPreview() {
        if (value == null || value.isBlank()) return;

        // Create video URL from file path
        video.setVideoSource(value);
        String videoSrc = video.getElement().getAttribute("src");

        getElement().executeJs("""
                    const video = document.createElement('video');
                    video.src = $0;
                    video.controls = true;
                    video.autoplay = true;
                    video.style.cssText = `
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
                    overlay.appendChild(video);
                    overlay.addEventListener('click',()=>overlay.remove());
                    document.body.appendChild(overlay);
                """, videoSrc);
    }

    private void setVideoFromPath(String fullPath) {
        // Extract just the filename from the absolute path
        Path path = Path.of(fullPath);
        String fileName = path.getFileName().toString();

        log.info("=== VIDEO UPLOAD DEBUG ===");
        log.info("Full path received: {}", fullPath);
        log.info("Extracted filename: {}", fileName);
        log.info("========================");

        // Generate thumbnail if using LocalVideoResourceProvider
        if (resourceProvider instanceof LocalVideoResourceProvider) {
            LocalVideoResourceProvider videoProvider = (LocalVideoResourceProvider) resourceProvider;

            // Check if the file actually exists
            Path videoPath = videoProvider.getPathForFile(fileName);
            log.info("Expected video location: {}", videoPath.toAbsolutePath());
            log.info("File exists: {}", Files.exists(videoPath));

            boolean thumbnailGenerated = videoProvider.generateThumbnailForVideo(fileName);
            if (thumbnailGenerated) {
                log.info("Thumbnail generated successfully for: {}", fileName);
            } else {
                log.warn("Failed to generate thumbnail for: {}. Check if FFmpeg is installed.", fileName);
            }
        }

        log.info("Setting value to database: {}", fileName);
        setValue(fileName);
    }

    private void clearVideo() {
        setValue(null);
    }

    private void loadThumbnail(String videoFileName) {
        if (videoFileName == null || videoFileName.isBlank()) {
            thumbnail.setSrc("");
            return;
        }

        if (resourceProvider instanceof LocalVideoResourceProvider) {
            LocalVideoResourceProvider videoProvider = (LocalVideoResourceProvider) resourceProvider;
            Path thumbnailPath = videoProvider.getThumbnailPath(videoFileName);

            if (Files.exists(thumbnailPath)) {
                // Load thumbnail as StreamResource
                String thumbnailFileName = videoProvider.getThumbnailFileName(videoFileName);
                StreamResource resource = new StreamResource(thumbnailFileName, () -> {
                    try {
                        return new FileInputStream(thumbnailPath.toFile());
                    } catch (FileNotFoundException e) {
                        log.error("Thumbnail file not found: {}", thumbnailPath, e);
                        return null;
                    }
                });
                thumbnail.setSrc(resource);
            } else {
                log.debug("Thumbnail not found for video: {}, will display placeholder", videoFileName);
                // TODO: Set placeholder image or icon
                thumbnail.setSrc("");
            }
        }
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
        // Preload video resource so it's registered for preview
        if (value != null && !value.isBlank()) {
            video.setVideoSource(value);
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
        // Preload video resource so it's registered for preview
        if (s != null && !s.isBlank()) {
            video.setVideoSource(s);
        }
        updateVisibility();
    }
}
