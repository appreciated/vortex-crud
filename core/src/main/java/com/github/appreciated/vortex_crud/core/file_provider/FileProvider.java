package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class FileProvider implements VortexCrudFileProvider {

    public FileProvider() {
    }

    @Override
    public StreamResource getResource(String src) {
        File file = new File(src);
        return new StreamResource(file.getName(), () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                try {
                    throw new RuntimeException("Cannot resolve file a path '%s'".formatted(file.getCanonicalFile().getPath()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public Path getPathForFile(String fileName) {
        return Path.of("images", fileName);
    }
}