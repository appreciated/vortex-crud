package com.github.appreciated.turbo_crud.core.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class FileProvider implements TurboCrudFileProvider {

    public FileProvider() {
    }

    @Override
    public StreamResource getResource(String src) {
        File file = new File(src);
        return new StreamResource(file.getName(), () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of("images", fileName);
    }
}