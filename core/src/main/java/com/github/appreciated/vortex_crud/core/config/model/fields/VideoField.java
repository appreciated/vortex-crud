package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.VideoFieldFactory;

import java.util.List;

public class VideoField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    private final RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;
    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;
    private List<String> writeRoles;
    private List<String> readOnlyRoles;

    public VideoField(RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration) {
        this.configuration = configuration;
        this.factory = VideoFieldFactory.class;
    }

    public VideoField(RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration, boolean required, Validation validation) {
        this(configuration);
        this.validation = validation;
        this.required = required;
    }

    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory) {
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

    @Override
    public void setWriteRoles(List<String> writeRoles) {
        this.writeRoles = writeRoles;
    }

    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        this.readOnlyRoles = readOnlyRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}