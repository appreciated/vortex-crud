package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.streams.DownloadHandler;

import java.nio.file.Path;

public interface VortexCrudResourceProvider {
    DownloadHandler getResource(String src);

    Path getPathForFile(String fileName);
}
