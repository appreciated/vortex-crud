package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.VideoFieldFactory;

public class VideoField<DataStoreId, FieldId, KeyType> implements Field<DataStoreId, FieldId, KeyType> {
    private final RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration;
    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;

    public VideoField(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration) {
        this.configuration = configuration;
        this.factory = VideoFieldFactory.class;
    }

    public VideoField(RouteRendererConfiguration<DataStoreId, FieldId, KeyType> configuration, boolean required, Validation validation) {
        this(configuration);
        this.validation = validation;
        this.required = required;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId, KeyType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>>) factory;
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