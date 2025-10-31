package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;

import java.nio.file.Path;

/**
 * This is the default image provider
 */
public class LocalImageResourceProvider implements VortexCrudResourceProvider {

    private final String basePath;

    public LocalImageResourceProvider(String basePath) {
        this.basePath = basePath;
    }

    public LocalImageResourceProvider() {
        this("images");
    }

    @Override
    public DownloadHandler getResource(String src) {
        return DownloadHandler.forFile(getPathForFile(src).toFile());
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of(basePath, fileName);
    }
}