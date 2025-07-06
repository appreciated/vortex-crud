package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

@GenerateBuilder
public class ImageFieldRendererConfiguration<DataStoreId, FieldId, ModelClass>  extends RouteRendererConfiguration<DataStoreId, FieldId, ModelClass>  {

    public ImageFieldRendererConfiguration(Class<? extends VortexCrudResourceProvider> fileFactory) {
        super(null);
        this.setImageFactory(fileFactory);
    }
}
