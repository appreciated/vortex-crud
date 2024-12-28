package com.github.appreciated.turbo_crud.core.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.nio.file.Path;

public interface TurboCrudFileProvider {
    StreamResource getResource(String src);

    Path getPathForFile(String fileName);
}
