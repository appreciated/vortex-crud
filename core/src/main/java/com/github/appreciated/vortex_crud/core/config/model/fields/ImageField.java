package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ImageFieldFactory;
import lombok.Builder;

import java.util.List;

@Builder
public record ImageField<ModelClass, FieldType, RepositoryType>(
        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    public ImageField(boolean required) {
        this(null, null, required, null, null);
    }

    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = ImageFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
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
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}