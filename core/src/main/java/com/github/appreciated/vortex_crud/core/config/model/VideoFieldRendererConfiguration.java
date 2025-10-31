package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class VideoFieldRendererConfiguration<ModelClass, FieldType, RepositoryType> extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    public VideoFieldRendererConfiguration(Class<? extends VortexCrudResourceProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
