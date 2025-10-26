package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import io.github.mletkin.numerobis.annotation.GenerateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@GenerateBuilder
public class RouteRenderer<ModelClass, FieldType, RepositoryType> implements AccessControlled {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean defaultRoute;

    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory;

    private boolean hideInMenu;

    private RouteConfig<FieldType> configuration;

    private Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap = new HashMap<>();

    private SerializableSupplier<Component> iconFactory;

    public RouteRenderer(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
    }

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    public RepositoryType getDataStoreKey() {
        return dataStoreKey;
    }

    public void setDataStoreKey(RepositoryType dataStoreKey) {
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

    public Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return factory;
    }

    public void setFactory(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
    }

    public boolean isHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(boolean hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public RouteConfig<FieldType> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RouteConfig<FieldType> configuration) {
        this.configuration = configuration;
    }

    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getChild() {
        return childrenMap.entrySet().stream().findFirst().orElseThrow().getValue();
    }

    public void setChild(RouteRenderer<ModelClass, FieldType, RepositoryType> child) {
        if (!childrenMap.isEmpty()) {
            throw new IllegalArgumentException("Route already has a child. Only one child is allowed when using setChild()");
        }
        this.childrenMap.put(null, child);
    }

    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public void setWriteRoles(List<String> writeRoles) {
        this.writeRoles = writeRoles;
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        this.readOnlyRoles = readOnlyRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }

    public abstract static class Builder<ModelClass, FieldType, RepositoryType> {

        private final RouteRenderer<ModelClass, FieldType, RepositoryType> product;

        public Builder(RouteRenderer<ModelClass, FieldType, RepositoryType> product) {
            this.product = product;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withDataStore(RepositoryType dataStore) {
            product.dataStoreKey = dataStore;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withTitle(String title) {
            product.title = title;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withIconFactory(SerializableSupplier<Component> iconFactory) {
            product.iconFactory = iconFactory;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withDefaultRoute(boolean defaultRoute) {
            product.defaultRoute = defaultRoute;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withHideInMenu(boolean hideInMenu) {
            product.hideInMenu = hideInMenu;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withConfiguration(RouteConfig<FieldType> configuration) {
            product.configuration = configuration;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChildrenMap(Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap) {
            product.childrenMap = childrenMap;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withChild(RouteRenderer<ModelClass, FieldType, RepositoryType> child) {
            product.setChild(child);
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withRoles(List<String> roles) {
            product.writeRoles = roles;
            return this;
        }

        public Builder<ModelClass, FieldType, RepositoryType> addRole(String item) {
            product.writeRoles.add(item);
            return this;
        }

        public RouteRenderer<ModelClass, FieldType, RepositoryType> build() {
            return product;
        }
    }
}
