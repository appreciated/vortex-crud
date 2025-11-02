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
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public ImageField(boolean required) {
        this(null, null, required, null, null, (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>)  ImageFieldFactory.class);
    }

    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }
}