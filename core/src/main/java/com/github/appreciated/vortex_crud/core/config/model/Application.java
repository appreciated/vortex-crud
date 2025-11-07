package com.github.appreciated.vortex_crud.core.config.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter // Needed for JpaVortexCrudConfigService
public class Application<ModelClass, FieldType, RepositoryType> {

    private String name;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores;

    private LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

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