package com.github.appreciated.vortex_crud.core.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

@GenerateBuilder
public abstract class Application<ModelClass, FieldType, RepositoryType> {

    private String name;

    private String i18nBundlePrefix;

    private IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement;

    private Selects selects;

    private Versioning<RepositoryType> versioning;

    private Auditing auditing;

    private Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores;

    private LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

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

    public IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> getUserManagement() {
        return identityAndAccessManagement;
    }

    public void setUserManagement(IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement) {
        this.identityAndAccessManagement = identityAndAccessManagement;
    }

    public Selects getSelects() {
        return selects;
    }

    public void setSelects(Selects selects) {
        this.selects = selects;
    }

    public Versioning<RepositoryType> getVersioning() {
        return versioning;
    }

    public void setVersioning(Versioning<RepositoryType> versioning) {
        this.versioning = versioning;
    }

    public Auditing getAuditing() {
        return auditing;
    }

    public void setAuditing(Auditing auditing) {
        this.auditing = auditing;
    }

    public Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> getDataStores() {
        return dataStores;
    }

    public void setDataStores(Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores) {
        this.dataStores = dataStores;
    }

    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> getRouteRenderers() {
        return routes;
    }

    public void setRouteRenderers(LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routeRenderers) {
        this.routes = routeRenderers;
    }

    public static class Builder<ModelClass, FieldType, RepositoryType> {

        private final Application<ModelClass, FieldType, RepositoryType> product;

        public Builder(Application<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withName(String name) {
            product.name = name;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withIdentityAndAccessManagement(IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> identityAndAccessManagement) {
            product.identityAndAccessManagement = identityAndAccessManagement;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withVersioning(Versioning<RepositoryType> versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withDataStores(Map<RepositoryType, DataStoreConfig<ModelClass, FieldType, RepositoryType>> dataStores) {
            product.dataStores = dataStores;
            return this;
        }

        public Application<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withRoutes(LinkedHashMap<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes) {
            product.routes = routes;
            return this;
        }
    }
}
