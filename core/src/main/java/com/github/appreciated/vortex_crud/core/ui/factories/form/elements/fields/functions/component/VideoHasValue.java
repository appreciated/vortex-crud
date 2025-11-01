package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.components.VideoDisplayComponent;
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

public class VideoHasValue extends CustomField<String> {

    private static final Logger log = LoggerFactory.getLogger(VideoHasValue.class);

    private final VortexCrudResourceProvider resourceProvider;
    private final VideoDisplayComponent video;
    private final VideoDisplayComponent thumbnailVideo;
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

        // Thumbnail video for display - uses preload="metadata" to show first frame
        thumbnailVideo = new VideoDisplayComponent(resourceProvider);
        thumbnailVideo.setPreload("metadata");
        thumbnailVideo.setMuted(true);
        thumbnailVideo.setSizeFull();
        thumbnailVideo.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "6px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("pointer-events", "none");  // Disable interaction with thumbnail video

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

        thumbWrapper = new Div(thumbnailVideo, overlay);
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

        log.info("Video uploaded: {}", fileName);
        setValue(fileName);
    }

    private void clearVideo() {
        setValue(null);
    }

    private void loadThumbnail(String videoFileName) {
        if (videoFileName == null || videoFileName.isBlank()) {
            thumbnailVideo.setVisible(false);
            return;
        }

        // Set the video source for the thumbnail - browser will automatically
        // display the first frame with preload="metadata"
        thumbnailVideo.setVideoSource(videoFileName);
        thumbnailVideo.setVisible(true);
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
