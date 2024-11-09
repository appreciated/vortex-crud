package com.github.appreciated.turbo_crud.file_provider;

import com.vaadin.flow.component.upload.receivers.FileFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.OutputStream;
import java.nio.file.Path;

public interface TurboCrudFileProvider {
    StreamResource getResource(String src);

    Path getPathForFile(String fileName);
}
