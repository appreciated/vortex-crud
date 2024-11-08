package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.ConfigObject;
import com.typesafe.config.Optional;

import java.util.Map;

import static com.github.appreciated.turbo_crud.config.model.ConfigModelUtil.toStringMapWithValueType;

public class Application {
    private String name;
    @Optional
    private UserManagement userManagement;
    @Optional
    private ConfigObject selects;
    @Optional
    private Versioning versioning;
    @Optional
    private Auditing auditing;
    private ConfigObject repositories;
    private ConfigObject routes;

    private String i18nBundlePrefix;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserManagement getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    public ConfigObject getSelects() {
        return selects;
    }

    public void setSelects(ConfigObject selects) {
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

    public ConfigObject getRepositories() {
        return repositories;
    }

    public void setRepositories(ConfigObject repositories) {
        this.repositories = repositories;
    }

    public Map<String, Repository> getRepositoriesConfig() {
        return toStringMapWithValueType(repositories, Repository.class);
    }

    public ConfigObject getRoutes() {
        return routes;
    }

    public void setRoutes(ConfigObject routes) {
        this.routes = routes;
    }

    public Map<String, Route> getRoutesConfig() {
        return toStringMapWithValueType(routes, Route.class);
    }

    public String getI18nBundlePrefix() {
        return i18nBundlePrefix;
    }

    public void setI18nBundlePrefix(String i18nBundlePrefix) {
        this.i18nBundlePrefix = i18nBundlePrefix;
    }
}
