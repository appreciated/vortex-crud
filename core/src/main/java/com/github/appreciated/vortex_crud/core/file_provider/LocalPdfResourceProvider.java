package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Default PDF file provider - stores PDFs in a local directory
 */
public class LocalPdfResourceProvider implements VortexCrudResourceProvider {

    private final String basePath;

    public LocalPdfResourceProvider(String basePath) {
        this.basePath = basePath;
        ensureStorageDirectoryExists();
    }

    public LocalPdfResourceProvider() {
        this("pdfs");
    }

    @Override
    public DownloadHandler getResource(String src) {
        return DownloadHandler.forFile(getPathForFile(src).toFile());
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of(basePath, fileName);
    }

    private void ensureStorageDirectoryExists() {
        try {
            Files.createDirectories(Path.of(basePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create PDF storage directory: " + basePath, e);
        }
    }
}
