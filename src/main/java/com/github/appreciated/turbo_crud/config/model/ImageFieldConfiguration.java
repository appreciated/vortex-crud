package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldConfiguration extends RouteConfiguration {

    private Class<? extends TurboCrudFileProviderRegistry> fileFactory;

    public ImageFieldConfiguration(Class<? extends TurboCrudFileProviderRegistry> fileFactory) {
        super(null);
        this.fileFactory = fileFactory;
    }

    public Class<? extends TurboCrudFileProviderRegistry> getFileFactory() {
        return fileFactory;
    }

    public void setFileFactory(Class<? extends TurboCrudFileProviderRegistry> fileFactory) {
        this.fileFactory = fileFactory;
    }
}
