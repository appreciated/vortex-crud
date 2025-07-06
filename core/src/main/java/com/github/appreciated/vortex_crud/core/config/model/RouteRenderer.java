package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class RouteRenderer<DataStoreId, FieldId, ModelClass> {

    private DataStoreId dataStore;

    private String title;

    private boolean defaultRoute;

    private Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> factory;

    private boolean hideInMenu;

    private RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration;

    private Map<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public RouteRenderer(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> factory) {
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

    public Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration) {
        this.configuration = configuration;
    }

    public Map<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public RouteRenderer<DataStoreId, FieldId, ModelClass> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(RouteRenderer<DataStoreId, FieldId, ModelClass> child) {
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

    public abstract static class Builder<DataStoreId, FieldId, ModelClass> {

        private final RouteRenderer<DataStoreId, FieldId, ModelClass> product;

        public  Builder(RouteRenderer<DataStoreId, FieldId, ModelClass> product) {
            this.product = product;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDataStore(DataStoreId dataStore) {
            product.dataStore = dataStore;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withConfiguration(RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChildrenMap(Map<String, RouteRenderer<DataStoreId, FieldId, ModelClass>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withChild(RouteRenderer<DataStoreId, FieldId, ModelClass> child) {
            product.setChild(child);
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> withRoles(List<String> roles) {
            product.roles = roles;
            return this;
        }

        public Builder<DataStoreId, FieldId, ModelClass> addRole(String item) {
            product.roles.add(item);
            return this;
        }

        public RouteRenderer<DataStoreId, FieldId, ModelClass> build() {
            return product;
        }
    }
}
