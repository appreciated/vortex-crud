package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resource provider for video files that automatically generates thumbnails.
 * Videos are stored in the configured directory, and thumbnails are generated
 * alongside with a "_thumb.jpg" suffix.
 */
public class LocalVideoResourceProvider implements VortexCrudResourceProvider {

    private static final Logger log = LoggerFactory.getLogger(LocalVideoResourceProvider.class);
    private static final String THUMBNAIL_SUFFIX = "_thumb.jpg";

    private final String basePath;
    private VideoThumbnailGenerator thumbnailGenerator;

    public LocalVideoResourceProvider(String basePath) {
        this.basePath = basePath;
        ensureStorageDirectoryExists();
    }

    public LocalVideoResourceProvider() {
        this("videos");
    }

    /**
     * Sets the thumbnail generator. This is called by Spring after bean creation.
     * Using setter injection to avoid circular dependencies.
     */
    public void setThumbnailGenerator(VideoThumbnailGenerator thumbnailGenerator) {
        this.thumbnailGenerator = thumbnailGenerator;
    }

    private void ensureStorageDirectoryExists() {
        try {
            Path path = Path.of(basePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Created video storage directory: {}", path.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create video storage directory: {}", basePath, e);
        }
    }

    @Override
    public DownloadHandler getResource(String src) {
        return DownloadHandler.forFile(new File(src));
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of(basePath, sanitizeFileName(fileName));
    }

    /**
     * Gets the path to the thumbnail file for a given video file.
     *
     * @param videoFileName The video filename
     * @return Path to the thumbnail file
     */
    public Path getThumbnailPath(String videoFileName) {
        String thumbnailName = getThumbnailFileName(videoFileName);
        return Path.of(basePath, thumbnailName);
    }

    /**
     * Generates and stores a thumbnail for the uploaded video.
     * This should be called after the video file has been saved.
     *
     * @param videoFileName The filename of the video
     * @return true if thumbnail generation was successful, false otherwise
     */
    public boolean generateThumbnailForVideo(String videoFileName) {
        if (thumbnailGenerator == null) {
            log.warn("VideoThumbnailGenerator not set. Thumbnails will not be generated.");
            return false;
        }

        Path videoPath = getPathForFile(videoFileName);
        Path thumbnailPath = getThumbnailPath(videoFileName);

        if (!Files.exists(videoPath)) {
            log.warn("Video file does not exist: {}", videoPath);
            return false;
        }

        boolean success = thumbnailGenerator.generateThumbnail(videoPath, thumbnailPath);

        if (!success) {
            log.warn("Failed to generate thumbnail for video: {}. Video will be displayed without thumbnail.", videoFileName);
        }

        return success;
    }

    /**
     * Generates the thumbnail filename from the video filename.
     *
     * @param videoFileName The video filename
     * @return The thumbnail filename
     */
    public String getThumbnailFileName(String videoFileName) {
        int lastDotIndex = videoFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return videoFileName.substring(0, lastDotIndex) + THUMBNAIL_SUFFIX;
        }
        return videoFileName + THUMBNAIL_SUFFIX;
    }

    /**
     * Sanitizes the filename to prevent directory traversal and other security issues.
     *
     * @param fileName The filename to sanitize
     * @return The sanitized filename
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "";
        }
        // Remove path separators and parent directory references
        return fileName.replaceAll("[/\\\\]", "_")
                .replaceAll("\\.\\.", "_");
    }

    /**
     * Deletes both the video file and its thumbnail.
     *
     * @param videoFileName The video filename to delete
     * @return true if both files were deleted successfully (or didn't exist), false otherwise
     */
    public boolean deleteVideoAndThumbnail(String videoFileName) {
        boolean videoDeleted = true;
        boolean thumbnailDeleted = true;

        Path videoPath = getPathForFile(videoFileName);
        if (Files.exists(videoPath)) {
            try {
                Files.delete(videoPath);
                log.info("Deleted video file: {}", videoPath);
            } catch (IOException e) {
                log.error("Failed to delete video file: {}", videoPath, e);
                videoDeleted = false;
            }
        }

        Path thumbnailPath = getThumbnailPath(videoFileName);
        if (Files.exists(thumbnailPath)) {
            try {
                Files.delete(thumbnailPath);
                log.info("Deleted thumbnail file: {}", thumbnailPath);
            } catch (IOException e) {
                log.error("Failed to delete thumbnail file: {}", thumbnailPath, e);
                thumbnailDeleted = false;
            }
        }

        return videoDeleted && thumbnailDeleted;
    }
}