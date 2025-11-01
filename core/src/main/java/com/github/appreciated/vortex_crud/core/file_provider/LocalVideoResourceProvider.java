package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resource provider for video files.
 * Videos are stored in the configured directory.
 */
public class LocalVideoResourceProvider implements VortexCrudResourceProvider {

    private static final Logger log = LoggerFactory.getLogger(LocalVideoResourceProvider.class);

    private final String basePath;

    public LocalVideoResourceProvider(String basePath) {
        this.basePath = basePath;
        ensureStorageDirectoryExists();
    }

    public LocalVideoResourceProvider() {
        this("videos");
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
        // src is just the filename, not the full path
        Path fullPath = getPathForFile(src);
        return DownloadHandler.forFile(fullPath.toFile());
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of(basePath, fileName);
    }
}