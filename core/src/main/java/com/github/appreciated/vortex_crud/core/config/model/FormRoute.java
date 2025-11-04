package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record FormRoute<ModelClass, FieldType, RepositoryType>(
    RepositoryType dataStoreKey,
    String title,
    boolean isDefaultRoute,
    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory,
    boolean isHiddenInMenu,
    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration,
    SerializableSupplier<Component> iconFactory,
    List<String> writeRoles,
    List<String> readOnlyRoles,
    List<? extends InternalFormElement<ModelClass, FieldType, RepositoryType>> children
) implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Override
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() {
        return (FormRendererConfiguration<ModelClass, FieldType, RepositoryType>) configuration;
    }

    @SuppressWarnings("unchecked")
    public static class FormRouteBuilder<ModelClass, FieldType, RepositoryType> {
        FormRouteBuilder() {
            factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) FormRouteFactory.class;
        }
    }

    public RepositoryType getDataStoreKey() {
        return dataStoreKey;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getWriteRoles() {
        return writeRoles;
    }

    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}