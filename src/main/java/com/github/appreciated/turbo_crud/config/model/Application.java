package com.github.appreciated.turbo_crud.config.model;

import java.util.Map;

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

    public Map<String, FormRoute> getForms() {
        return forms;
    }

    public void setForms(Map<String, FormRoute> forms) {
        this.forms = forms;
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, Route> routes) {
        this.routes = routes;
    }
}
