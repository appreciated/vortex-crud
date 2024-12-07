package com.github.appreciated.turbo_crud.config.model;

import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class Route {

    private String repository;

    private String title;

    private boolean defaultRoute;

    private Class<? extends TurboCrudRouteFactory> factory;

    private boolean hideInMenu;

    private RouteConfiguration configuration;

    private Map<String, Route> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public Route(Class<? extends TurboCrudRouteFactory> factory) {
        this.factory = factory;
    }

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

    public SerializableSupplier<Component> getIconFactory() {
        return iconFactory;
    }

    public void setIconFactory(SerializableSupplier<Component> iconFactory) {
        this.iconFactory = iconFactory;
    }

    public boolean isDefaultRoute() {
        return defaultRoute;
    }

    public void setDefaultRoute(boolean defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    public Class<? extends TurboCrudRouteFactory> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudRouteFactory> factory) {
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
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(Route child) {
        if (!childrenMap.isEmpty()){
            throw new IllegalArgumentException("Route already has a child. Only one child is allowed when using setChild()");
        }
        this.childrenMap.put(null,child);
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

        public static Builder of(Class<? extends TurboCrudRouteFactory> factory) {
            return new Builder(new Route(factory));
        }

        public Builder withRepository(String repository) {
            product.repository = repository;
            return this;
        }

        public Builder withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
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
            product.setChild(child);
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
