package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldConfiguration extends RouteConfiguration {

    public ImageFieldConfiguration(Class<? extends TurboCrudFileProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
