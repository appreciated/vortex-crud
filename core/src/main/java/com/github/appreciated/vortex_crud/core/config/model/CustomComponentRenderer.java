package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * Functional interface for rendering custom components in a CustomRoute.
 * Provides access to the necessary context and services for building custom views.
 *
 * @param <ModelClass>     The entity model class
 * @param <FieldType>      The field type (typically an enum or descriptor)
 * @param <RepositoryType> The repository/data store key type
 */
@FunctionalInterface
public interface CustomComponentRenderer<ModelClass, FieldType, RepositoryType> extends Serializable {

    /**
     * Renders a custom component for the route.
     *
     * @param context The rendering context containing all necessary services and configuration
     * @return The rendered component
     */
    Component render(RenderContext<ModelClass, FieldType, RepositoryType> context);

    /**
     * Context object providing access to all services and configuration needed for rendering.
     */
    class RenderContext<ModelClass, FieldType, RepositoryType> implements Serializable {
        private final Integer currentPathIndex;
        private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver;
        private final DetailRouteSetting detailRouteSetting;
        private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
        private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
        private final CustomRoute<ModelClass, FieldType, RepositoryType> route;

        public RenderContext(
                Integer currentPathIndex,
                VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                @Nullable DetailRouteSetting detailRouteSetting,
                VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                CustomRoute<ModelClass, FieldType, RepositoryType> route
        ) {
            this.currentPathIndex = currentPathIndex;
            this.routeResolver = routeResolver;
            this.detailRouteSetting = detailRouteSetting;
            this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
            this.configService = configService;
            this.route = route;
        }

        public Integer getCurrentPathIndex() {
            return currentPathIndex;
        }

        public VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> getRouteResolver() {
            return routeResolver;
        }

        @Nullable
        public DetailRouteSetting getDetailRouteSetting() {
            return detailRouteSetting;
        }

        public VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> getDataStoreFactoryRegistry() {
            return dataStoreFactoryRegistry;
        }

        public VortexCrudConfigService<ModelClass, FieldType, RepositoryType> getConfigService() {
            return configService;
        }

        public CustomRoute<ModelClass, FieldType, RepositoryType> getRoute() {
            return route;
        }

        /**
         * Convenience method to get the data store for the current route.
         */
        public VortexCrudDataStore<FieldType, ModelClass> getDataStore() {
            return dataStoreFactoryRegistry.getDataStore(route.dataStoreKey());
        }

        /**
         * Convenience method to get the data store configuration for the current route.
         */
        public DataStoreConfig<ModelClass, FieldType, RepositoryType> getDataStoreConfig() {
            return configService.configuration().dataStores().get(route.dataStoreKey());
        }
    }
}
