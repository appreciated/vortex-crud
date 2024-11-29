package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.ConfigObject;

import java.util.List;
import java.util.Map;

public class Route {
    private String repository;
    private String title;
    private String icon;
    private boolean defaultRoute;
    private String factory;
    private boolean hideInMenu;
    private RouteConfiguration configuration;
    private ConfigObject children;
    private Map<String, Route> childrenMap;
    private Route child;
    private List<String> roles;

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
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

    public RouteConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfiguration configuration) {
        this.configuration = configuration;
    }

    public ConfigObject getChildren() {
        return children;
    }

    public void setChildren(ConfigObject children) {
        this.children = children;
    }

    public Map<String, Route> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, Route> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public Route getChild() {
        return child;
    }

    public void setChild(Route child) {
        this.child = child;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}



