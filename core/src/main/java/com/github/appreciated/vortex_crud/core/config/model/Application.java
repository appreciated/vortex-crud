package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.menu.MenuActionComponentFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
public class Application<ModelClass, FieldType, RepositoryType> {

    private String name;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores;

    private LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

    /**
     * Default menu action component factories that will be applied to all routes.
     * These can be overridden or supplemented by route-specific menu action factories.
     */
    private List<MenuActionComponentFactory<ModelClass, FieldType, RepositoryType>> defaultMenuActionFactories;

    public String getApplicationName() {
        return name;
    }

    public IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> getUserManagement() {
        return identityAndAccessManagement;
    }

    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> getRouteRenderers() {
        return routes;
    }

    public String getI18nBundlePrefix() {
        return i18nBundlePrefix;
    }

    public Selects getSelects() {
        return selects;
    }

    public Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> getDataStores() {
        return dataStores;
    }
}