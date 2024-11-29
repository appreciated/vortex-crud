package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.Map;

@GenerateBuilder
public class Application {

    private String name;

    private String i18nBundlePrefix;

    private UserManagement userManagement;

    private Selects selects;

    private Versioning versioning;

    private Auditing auditing;

    private Map<String, Repository> repositories;

    private Map<String, FormRoute> forms;

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

    public Map<String, Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(Map<String, Repository> repositories) {
        this.repositories = repositories;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }

    public static class Builder {

        private Application product;

        private Builder(Application product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Application());
        }

        public Builder withName(String name) {
            product.name = name;
            return this;
        }

        public Builder withI18nBundlePrefix(String i18nBundlePrefix) {
            product.i18nBundlePrefix = i18nBundlePrefix;
            return this;
        }

        public Builder withUserManagement(UserManagement userManagement) {
            product.userManagement = userManagement;
            return this;
        }

        public Builder withSelects(Selects selects) {
            product.selects = selects;
            return this;
        }

        public Builder withVersioning(Versioning versioning) {
            product.versioning = versioning;
            return this;
        }

        public Builder withAuditing(Auditing auditing) {
            product.auditing = auditing;
            return this;
        }

        public Builder withRepositories(Map<String, Repository> repositories) {
            product.repositories = repositories;
            return this;
        }

        public Application build() {
            return product;
        }
    }
}
