package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class Route<DataStoreId> {

    private DataStoreId dataStore;

    private String title;

    private boolean defaultRoute;

    private Class<? extends TurboCrudRouteFactory<DataStoreId>> factory;

    private boolean hideInMenu;

    private RouteConfiguration<DataStoreId> configuration;

    private Map<String, Route<DataStoreId>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public Route(Class<? extends TurboCrudRouteFactory<DataStoreId>> factory) {
        this.factory = factory;
    }

    private List<String> roles;

    public DataStoreId getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStoreId dataStore) {
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

    public Class<? extends TurboCrudRouteFactory<DataStoreId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends TurboCrudRouteFactory<DataStoreId>> factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public RouteConfiguration<DataStoreId> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfiguration<DataStoreId> configuration) {
        this.configuration = configuration;
    }

    public Map<String, Route<DataStoreId>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, Route<DataStoreId>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public Route<DataStoreId> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(Route<DataStoreId> child) {
        if (!childrenMap.isEmpty()) {
            throw new IllegalArgumentException("Route already has a child. Only one child is allowed when using setChild()");
        }
        this.childrenMap.put(null, child);
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static class Builder<DataStoreId> {

        private Route<DataStoreId> product;

        public Builder(Route<DataStoreId> product) {
            this.product = product;
        }

        public Builder<DataStoreId> of(Class<? extends TurboCrudRouteFactory<DataStoreId>> factory) {
            return new Builder<>(new Route<>(factory));
        }

        public Builder<DataStoreId> withDataStore(DataStoreId dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<DataStoreId> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<DataStoreId> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<DataStoreId> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<DataStoreId> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<DataStoreId> withConfiguration(RouteConfiguration<DataStoreId> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId> withChildrenMap(Map<String, Route<DataStoreId>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<DataStoreId> withChild(Route<DataStoreId> child) {
            product.setChild(child);
            return this;
        }

        public Builder<DataStoreId> withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder<DataStoreId> addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public Route<DataStoreId> build() {
            return product;
        }
    }
}
