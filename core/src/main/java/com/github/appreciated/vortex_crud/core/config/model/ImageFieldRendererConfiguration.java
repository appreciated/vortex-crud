package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldRendererConfiguration<DataStoreId, FieldId> extends RouteRendererConfiguration<DataStoreId, FieldId> {

    public ImageFieldRendererConfiguration(Class<? extends VortexCrudFileProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
