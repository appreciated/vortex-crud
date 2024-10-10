package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

public class RouteConfig {

    private String table;
    private String title;
    private String factory;
    @Optional
    private String icon;
    @Optional
    private boolean defaultRoute;
    private FactoryConfig factoryConfiguration;
    @Optional
    private AccessControlConfig accessControl;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public FactoryConfig getFactoryConfiguration() {
        return factoryConfiguration;
    }

    public void setFactoryConfiguration(FactoryConfig factoryConfiguration) {
        this.factoryConfiguration = factoryConfiguration;
    }

    public AccessControlConfig getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControlConfig accessControl) {
        this.accessControl = accessControl;
    }

    public boolean isDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(boolean defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

