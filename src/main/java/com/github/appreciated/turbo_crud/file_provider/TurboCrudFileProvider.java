package com.github.appreciated.turbo_crud.file_provider;

import com.vaadin.flow.server.StreamResource;

public interface TurboCrudFileProvider {
    StreamResource getResource(String src);
}
