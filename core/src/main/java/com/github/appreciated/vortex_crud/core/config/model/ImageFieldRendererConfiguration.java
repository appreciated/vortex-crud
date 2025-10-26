package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldRendererConfiguration<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    public ImageFieldRendererConfiguration(Class<? extends VortexCrudResourceProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
