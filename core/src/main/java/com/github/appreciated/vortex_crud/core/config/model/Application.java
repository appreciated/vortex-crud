package com.github.appreciated.vortex_crud.core.config.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public abstract class Application<ModelClass, FieldType, RepositoryType> {

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
}