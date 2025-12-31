package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
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

    @I18nKey
    private String title;
    private boolean isDefaultRoute;
    private boolean isHiddenInMenu;
    private SerializableSupplier<Component> iconFactory;

    /**
     * Optional list of specific routes to search within.
     * If provided, only these routes will be searched.
     * If null or empty, all routes with dataStoreConfig and filterField will be searched.
     */
    private List<RouteRenderer<ModelClass, FieldType, RepositoryType>> searchableRoutes;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new SearchRouteFactory<>();

    @Override
    public DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig() {
        return null;
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
