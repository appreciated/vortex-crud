package com.github.appreciated.vortex_crud.core.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * This is the default image provider
 */
public class ImageResourceProvider implements VortexCrudResourceProvider {

    private final String basePath;

    public ImageResourceProvider(String basePath) {
        this.basePath = basePath;
    }

    public ImageResourceProvider() {
        this("images");
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
        return Path.of(basePath, fileName);
    }
}