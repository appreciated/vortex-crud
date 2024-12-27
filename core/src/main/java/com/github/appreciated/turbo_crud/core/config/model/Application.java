package com.github.appreciated.turbo_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.Map;

@GenerateBuilder
public class Application<T> {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement userManagement;

    private Selects selects;

    private Versioning versioning;

    private Auditing auditing;

    private Map<T, DataStoreConfig<?>> dataStores;

    private Map<String, Route> routes;

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

    public Map<T, DataStoreConfig<?>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(Map<T, DataStoreConfig<?>> dataStores) {
        this.dataStores = dataStores;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public static class Builder<T> {

        private Application<T> product;

        public Builder(Application<T> product) {
            this.product = product;
        }

        public Builder<T> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<T> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<T> withUserManagement(UserManagement userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder<T> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<T> withVersioning(Versioning versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<T> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<T> withDataStores(Map<T, DataStoreConfig<?>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<T> build() {
            return product;
        }

        public Builder<T> withRoutes(Map<String, Route> routes) {
            product.routes = routes;
            return this;
        }
    }
}
