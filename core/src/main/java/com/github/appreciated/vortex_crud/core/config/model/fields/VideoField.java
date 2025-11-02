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
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> getConfiguration() {
        return configuration;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = VideoFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
    }

}