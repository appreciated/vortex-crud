package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@GenerateBuilder
public abstract class Application<DataStoreId, FieldId, ModelClass> {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement userManagement;

    private Selects selects;

    private Versioning<DataStoreId> versioning;

    private Auditing auditing;

    private Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> dataStores;

    private LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> routes;

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

    public Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> dataStores) {
        this.dataStores = dataStores;
    }

    public Map<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> getRouteRenderers() {
        return routes;
    }

    public void setRouteRenderers(LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> routeRenderers) {
        this.routes = routeRenderers;
    }

    public static class Builder<DataStoreId, FieldId, ModelClass> {

        private final Application<DataStoreId,FieldId, ModelClass> product;

        public Builder(Application<DataStoreId,FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withUserManagement(UserManagement userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withVersioning(Versioning<DataStoreId> versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDataStores(Map<DataStoreId, DataStoreConfig<DataStoreId, FieldId, ModelClass>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withRoutes(LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> routes) {
            product.routes = routes;
            return this;
        }
    }
}
