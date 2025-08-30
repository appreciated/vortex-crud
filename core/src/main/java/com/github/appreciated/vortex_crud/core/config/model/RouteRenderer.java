package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class RouteRenderer<DataStoreId, FieldId, KeyType> {

    private KeyType dataStoreKey;

    private String title;

    private boolean defaultRoute;

    private Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> factory;

    private boolean hideInMenu;

    private RouteConfig<DataStoreId, FieldId, KeyType> configuration;

    private Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public RouteRenderer(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    private List<String> roles;

    public KeyType getDataStoreKey() {
        return dataStoreKey;
    }

    public void setDataStoreKey(KeyType dataStoreKey) {
        this.dataStoreKey = dataStoreKey;
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

    public Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public RouteConfig<DataStoreId, FieldId, KeyType> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfig<DataStoreId, FieldId, KeyType> configuration) {
        this.configuration = configuration;
    }

    public Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public RouteRenderer<DataStoreId, FieldId, KeyType> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId, KeyType> child) {
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

    public abstract static class Builder<DataStoreId, FieldId, KeyType> {

        private final RouteRenderer<DataStoreId, FieldId, KeyType> product;

        public Builder(RouteRenderer<DataStoreId, FieldId, KeyType> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, KeyType> withDataStore(KeyType dataStore) {
            product.dataStoreKey = dataStore;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withConfiguration(RouteConfig<DataStoreId, FieldId, KeyType> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withChild(RouteRenderer<DataStoreId, FieldId, KeyType> child) {
            product.setChild(child);
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder<DataStoreId, FieldId, KeyType> addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public RouteRenderer<DataStoreId, FieldId, KeyType> build() {
            return product;
        }
    }
}
