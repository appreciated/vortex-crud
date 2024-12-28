package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class Route<T> {

    private T dataStore;

    private String title;

    private boolean defaultRoute;

    private Class<? extends TurboCrudRouteFactory> factory;

    private boolean hideInMenu;

    private RouteConfiguration configuration;

    private Map<String, Route<T>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public Route(Class<? extends TurboCrudRouteFactory> factory) {
        this.factory = factory;
    }

    private List<String> roles;

    public T getDataStore() {
        return dataStore;
    }

    public void setDataStore(T dataStore) {
        this.dataStore = dataStore;
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

    public Map<String, Route<T>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, Route<T>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public Route<T> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(Route<T> child) {
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

    public static class Builder<T> {

        private Route<T> product;

        public Builder(Route<T> product) {
            this.product = product;
        }

        public Builder<T> of(Class<? extends TurboCrudRouteFactory> factory) {
            return new Builder<>(new Route<T>(factory));
        }

        public Builder<T> withDataStore(T dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<T> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<T> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<T> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<T> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<T> withConfiguration(RouteConfiguration configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<T> withChildrenMap(Map<String, Route<T>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<T> withChild(Route<T> child) {
            product.setChild(child);
            return this;
        }

        public Builder<T> withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder<T> addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public Route<T> build() {
            return product;
        }
    }
}
