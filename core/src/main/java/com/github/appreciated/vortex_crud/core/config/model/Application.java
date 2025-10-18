package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.User;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@GenerateBuilder
public abstract class Application<DataStoreId, FieldId, KeyType, U extends User> {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement<U> userManagement;

    private Selects selects;

    private Versioning versioning;

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

    public UserManagement<U> getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagement<U> userManagement) {
        this.userManagement = userManagement;
    }

    public Selects getSelects() {
        return selects;
    }

    public void setSelects(Selects selects) {
        this.selects = selects;
    }

    public Versioning getVersioning() {
        return versioning;
    }

    public void setVersioning(Versioning versioning) {
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

    public static class Builder<DataStoreId, FieldId, KeyType, U extends User> {

        private final Application<DataStoreId, FieldId, KeyType, U> product;

        public Builder(Application<DataStoreId, FieldId, KeyType, U> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withUserManagement(UserManagement<U> userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withVersioning(Versioning versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withDataStores(Map<KeyType, DataStoreConfig<DataStoreId, FieldId, KeyType>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<DataStoreId, FieldId, KeyType, U> build() {
            return product;
        }

        public Builder<DataStoreId, FieldId, KeyType, U> withRoutes(LinkedHashMap<String, RouteRenderer<DataStoreId, FieldId, KeyType>> routes) {
            product.routes = routes;
            return this;
        }
    }
}
