package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldRendererConfiguration<DataStoreId, FieldId> extends RouteRendererConfiguration<DataStoreId, FieldId> {

    public ImageFieldRendererConfiguration(Class<? extends TurboCrudFileProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
