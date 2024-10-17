package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Config;
import com.typesafe.config.Optional;

import java.util.List;
import java.util.Map;

public class Route {
    private String table;
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
    private Map<String, Route> children;
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

    public Map<String, Route> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Route> children) {
        this.children = children;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Route getChild() {
        assert children.size() < 2;
        return children.values().stream().findFirst().orElse(null);
    }

    public void setChild(Route child) {
        this.children = Map.of("", child);
    }
}