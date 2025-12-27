package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.search.SearchRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SearchRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    private String title;
    private boolean isDefaultRoute;
    private boolean isHiddenInMenu;
    private SerializableSupplier<Component> iconFactory;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new SearchRouteFactory<>();

    @Override
    public DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig() {
        return null;
    }

    @Override
    public List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions() {
        return Collections.emptyList();
    }

    @Override
    public List<String> writeRoles() {
        return null;
    }

    @Override
    public List<String> readOnlyRoles() {
        return null;
    }
}
