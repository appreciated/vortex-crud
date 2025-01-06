package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.nio.file.Path;

public interface VortexCrudFileProvider {
    StreamResource getResource(String src);

    Path getPathForFile(String fileName);
}
