package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.VideoFieldFactory;
import lombok.Builder;

import java.util.List;

@Builder
public record VideoField<ModelClass, FieldType, RepositoryType>(
        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public static class VideoFieldBuilder<ModelClass, FieldType, RepositoryType> {
        VideoFieldBuilder() {
            factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) VideoFieldFactory.class;
        }
    }

    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }

}