package com.github.appreciated.vortex_crud.core.ui.factories.route.custom;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

/**
 * No-op factory for CustomRoute. This factory should never actually be called since CustomRoute
 * instances are handled by Vaadin's @Route annotation system and are excluded from dynamic route registration.
 * <p>
 * This class exists purely for interface compatibility with the RouteRenderer interface.
 *
 * @param <ModelClass>     The entity model class
 * @param <FieldType>      The field type (typically an enum or descriptor)
 * @param <RepositoryType> The repository/data store key type
 */
public class CustomRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    public CustomRouteFactory(
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService
    ) {
        // No-op constructor for Spring dependency injection compatibility
    }

    @Override
    public Component renderRoute(
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        throw new UnsupportedOperationException(
                "CustomRouteFactory.renderRoute() should never be called. " +
                        "CustomRoute uses Vaadin's @Route annotation system for routing. " +
                        "Ensure your custom view is annotated with @Route and the path matches the configuration key."
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
