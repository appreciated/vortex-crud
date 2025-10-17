package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ImageFieldFactory;

public class ImageField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration;
    private Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory;
    private Validation validation;
    private boolean required;

    public ImageField(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration) {
        this.configuration = configuration;
        this.factory = (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) ImageFieldFactory.class;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId, KeyType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    @Override
    public Validation getValidation() {
        return validation;
    }

    @Override
    public boolean isRequired() {
        return required;
    }
}