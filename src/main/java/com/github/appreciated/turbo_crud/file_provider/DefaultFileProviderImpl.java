package com.github.appreciated.turbo_crud.file_provider;

import com.vaadin.flow.server.StreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DefaultFileProviderImpl implements TurboCrudFileProvider {

    public DefaultFileProviderImpl() {
    }

    @Override
    public StreamResource getResource(String src) {
        return new StreamResource(src.substring(src.lastIndexOf("/") + 1), () -> {
            try {
                return new FileInputStream(src);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

}