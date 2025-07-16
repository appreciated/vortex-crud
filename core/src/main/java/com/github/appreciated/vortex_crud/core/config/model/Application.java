package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@GenerateBuilder
public abstract class Application<DataStoreId, FieldId, KeyType> {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement userManagement;

    private Selects selects;

    private Versioning<DataStoreId> versioning;

    private Auditing auditing;

    private Map<KeyType, DataStoreConfig<DataStoreId, FieldId, KeyType>> dataStores;

    private LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, KeyType>> routes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getI18nBundlePrefix() {
        return i18nBundlePrefix;
    }

    public void setI18nBundlePrefix(String i18nBundlePrefix) {
        this.i18nBundlePrefix = i18nBundlePrefix;
    }

    public UserManagement getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    public Selects getSelects() {
        return selects;
    }

    public void setSelects(Selects selects) {
        this.selects = selects;
    }

    public Versioning<DataStoreId> getVersioning() {
        return versioning;
    }

    public void setVersioning(Versioning<DataStoreId> versioning) {
        this.versioning = versioning;
    }

    public Auditing getAuditing() {
        return auditing;
    }

    public void setAuditing(Auditing auditing) {
        this.auditing = auditing;
    }

    public Map<KeyType, DataStoreConfig<DataStoreId, FieldId, KeyType>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(Map<KeyType, DataStoreConfig<DataStoreId, FieldId, KeyType>> dataStores) {
        this.dataStores = dataStores;
    }

    public Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> getRouteRenderers() {
        return routes;
    }

    public void setRouteRenderers(LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, KeyType>> routeRenderers) {
        this.routes = routeRenderers;
    }

    public static class Builder<DataStoreId, FieldId, KeyType> {

        private final Application<DataStoreId, FieldId, KeyType> product;

        public Builder(Application<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withUserManagement(UserManagement userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withVersioning(Versioning<DataStoreId> versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withDataStores(Map<KeyType, DataStoreConfig<DataStoreId, FieldId, KeyType>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<DataStoreId, FieldId, KeyType> build() {
            return product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withRoutes(LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, KeyType>> routes) {
            product.routes = routes;
            return this;
        }
    }
}
