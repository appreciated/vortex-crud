package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigObject;
import com.typesafe.config.Optional;

import java.util.Map;

import static com.github.appreciated.flow_cms.config.model.ConfigModelUtil.toStringMapWithValueType;

public class ApplicationConfig {
    private String name;
    @Optional
    private UserManagementConfig userManagement;
    @Optional
    private ConfigObject selects;
    @Optional
    private VersioningConfig versioning;
    @Optional
    private AuditingConfig auditing;
    private ConfigObject tables;
    private ConfigObject routes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserManagementConfig getUserManagement() {
        return userManagement;
    }

    public void setUserManagement(UserManagementConfig userManagement) {
        this.userManagement = userManagement;
    }

    public ConfigObject getSelects() {
        return selects;
    }

    public void setSelects(ConfigObject selects) {
        this.selects = selects;
    }

    public Map<String, SelectConfig> getSelectsConfig() {
        return toStringMapWithValueType(selects, SelectConfig.class);
    }

    public VersioningConfig getVersioning() {
        return versioning;
    }

    public void setVersioning(VersioningConfig versioning) {
        this.versioning = versioning;
    }

    public AuditingConfig getAuditing() {
        return auditing;
    }

    public void setAuditing(AuditingConfig auditing) {
        this.auditing = auditing;
    }

    public ConfigObject getTables() {
        return tables;
    }

    public void setTables(ConfigObject tables) {
        this.tables = tables;
    }

    public Map<String, TableConfig> getTablesConfig() {
        return toStringMapWithValueType(tables, TableConfig.class);
    }

    public ConfigObject getRoutes() {
        return routes;
    }

    public void setRoutes(ConfigObject routes) {
        this.routes = routes;
    }

    public Map<String, RouteConfig> getRoutesConfig() {
        return toStringMapWithValueType(routes, RouteConfig.class);
    }

}
