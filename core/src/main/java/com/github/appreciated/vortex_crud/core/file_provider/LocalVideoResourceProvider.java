package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;

import java.io.File;
import java.nio.file.Path;

/**
 * This is the default image provider
 */
public class LocalVideoResourceProvider implements VortexCrudResourceProvider {

    private final String basePath;

    public LocalVideoResourceProvider(String basePath) {
        this.basePath = basePath;
    }

    public LocalVideoResourceProvider() {
        this("videos");
    }

    @Override
    public DownloadHandler getResource(String src) {
        return DownloadHandler.forFile(new File(src));
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of(basePath, fileName);
    }
}