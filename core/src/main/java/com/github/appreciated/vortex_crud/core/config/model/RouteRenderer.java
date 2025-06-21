package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class RouteRenderer<DataStoreId, FieldId> {

    private DataStoreId dataStore;

    private String title;

    private boolean defaultRoute;

    private Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> factory;

    private boolean hideInMenu;

    private RouteRendererConfiguration<DataStoreId, FieldId> configuration;

    private Map<String, RouteRenderer<DataStoreId, FieldId>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public RouteRenderer(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> factory) {
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

    public Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteRendererConfiguration<DataStoreId, FieldId> configuration) {
        this.configuration = configuration;
    }

    public Map<String, RouteRenderer<DataStoreId, FieldId>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public RouteRenderer<DataStoreId, FieldId> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId> child) {
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

    public abstract static class Builder<DataStoreId, FieldId> {

        private final RouteRenderer<DataStoreId, FieldId> product;

        public  Builder(RouteRenderer<DataStoreId, FieldId> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId> withDataStore(DataStoreId dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<DataStoreId, FieldId> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<DataStoreId, FieldId> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<DataStoreId, FieldId> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<DataStoreId, FieldId> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<DataStoreId, FieldId> withConfiguration(RouteRendererConfiguration<DataStoreId, FieldId> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<DataStoreId, FieldId> withChild(RouteRenderer<DataStoreId, FieldId> child) {
            product.setChild(child);
            return this;
        }

        public Builder<DataStoreId, FieldId> withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder<DataStoreId, FieldId> addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public RouteRenderer<DataStoreId, FieldId> build() {
            return product;
        }
    }
}
