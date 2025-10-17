package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

public class ImageField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration;

    public ImageField(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration) {
        this.configuration = configuration;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId, KeyType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return null;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {

    }

    @Override
    public Validation getValidation() {
        return null;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
