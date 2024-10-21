package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.Optional;

import java.util.List;
import java.util.Map;

public class Route {
    @Optional
    private String table;
    @Optional
    private String title;
    @Optional
    private String icon;
    @Optional
    private boolean defaultRoute;
    private String factory;
    @Optional
    private boolean hideInMenu;
    @Optional
    private Config configuration;
    @Optional
    private ConfigObject children;
    @Optional
    private Map<String, Route> childrenMap;
    @Optional
    private Route child;
    @Optional
    private List<String> roles;


    // Getter und Setter
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(boolean defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public Config getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Config configuration) {
        this.configuration = configuration;
    }

    public ConfigObject getChildren() {
        return children;
    }

    public Map<String, Route> getChildrenMap() {
        return childrenMap;
    }

    public void setChildren(ConfigObject children) {
        this.children = children;
        this.childrenMap = ConfigModelUtil.toStringMapWithValueType(this.children, Route.class);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Route getChild() {
        assert childrenMap.size() < 2;
        return childrenMap.values().stream().findFirst().orElse(null);
    }

    public void setChild(Route child) {
        this.childrenMap = Map.of("", child);
    }
}