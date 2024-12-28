package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class Application<DataStoreId, FieldId> {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement userManagement;

    private Selects selects;

    private Versioning versioning;

    private Auditing auditing;

    private Map<DataStoreId, DataStoreConfig<FieldId>> dataStores;

    private Map<String, Route<DataStoreId, FieldId>> routes;

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

    public Map<DataStoreId, DataStoreConfig<FieldId>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(Map<DataStoreId, DataStoreConfig<FieldId>> dataStores) {
        this.dataStores = dataStores;
    }

    public Map<String, Route<DataStoreId, FieldId>> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route<DataStoreId, FieldId>> routes) {
        this.routes = routes;
    }

    public static class Builder<DataStoreId, FieldId> {

        private Application<DataStoreId,FieldId> product;

        public Builder(Application<DataStoreId,FieldId> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<DataStoreId, FieldId> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<DataStoreId, FieldId> withUserManagement(UserManagement userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder<DataStoreId, FieldId> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<DataStoreId, FieldId> withVersioning(Versioning versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<DataStoreId, FieldId> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<DataStoreId, FieldId> withDataStores(Map<DataStoreId, DataStoreConfig<FieldId>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<DataStoreId, FieldId> build() {
            return product;
        }

        public Builder<DataStoreId, FieldId> withRoutes(Map<String, Route<DataStoreId, FieldId>> routes) {
            product.routes = routes;
            return this;
        }
    }
}
