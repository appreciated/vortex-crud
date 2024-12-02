package com.github.appreciated.turbo_crud.config.model;

import io.github.mletkin.numerobis.annotation.GenerateBuilder;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class Route {

    private String repository;

    private String title;

    private String icon;

    private boolean defaultRoute;

    private String factory;

    private boolean hideInMenu;

    private RouteConfiguration configuration;

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

    public static class Builder {

        private Route product;

        Builder(Route product) {
            this.product = product;
        }

        public static Builder of() {
            return new Builder(new Route());
        }

        public Builder withRepository(String repository) {
            product.repository = repository;
            return this;
        }

        public Builder withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder withIcon(String icon) {
            product.icon = icon;
            return this;
        }

        public Builder withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder withFactory(String factory) {
            product.factory = factory;
            return this;
        }

        public Builder withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder withConfiguration(RouteConfiguration configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder withChildrenMap(Map<String, Route> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder withChild(Route child) {
            product.child = child;
            return this;
        }

        public Builder withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public Route build() {
            return product;
        }
    }
}
