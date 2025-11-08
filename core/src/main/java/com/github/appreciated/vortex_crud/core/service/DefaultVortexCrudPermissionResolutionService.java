package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import org.springframework.stereotype.Service;

/**
 * Default implementation of VortexCrudPermissionResolutionService.
 * Uses VortexCrudPathToRouteResolver for proper nested route handling.
 */
@Service
public class DefaultVortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType>
        implements VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public DefaultVortexCrudPermissionResolutionService(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
            VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public RouteRenderer<ModelClass, FieldType, RepositoryType> resolveRouteForPath(String path) {
        // Handle null or empty path
        if (path == null || path.isEmpty() || path.equals("/")) {
            return findDefaultRoute();
        }

        // Remove leading slash if present
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;

        // If path is still empty after normalization, find default route
        if (normalizedPath.isEmpty()) {
            return findDefaultRoute();
        }

        try {
            // Use VortexCrudPathToRouteResolver to properly resolve nested routes
            VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> resolver =
                    new VortexCrudPathToRouteResolver<>(
                            routeFactoryRegistry,
                            normalizedPath,
                            configService.configuration().getRouteRenderers(),
                            dataStoreUtil
                    );

            return resolver.getCurrentRoute();
        } catch (Exception e) {
            // If resolution fails (e.g., route not found), return null
            // This allows non-vortex-crud routes to pass through
            return null;
        }
    }

    /**
     * Finds the default route from the configuration.
     *
     * @return The default route, or null if none is marked as default
     */
    private RouteRenderer<ModelClass, FieldType, RepositoryType> findDefaultRoute() {
        if (configService == null || configService.configuration() == null) {
            return null;
        }

        return configService.configuration().getRouteRenderers().values().stream()
                .filter(RouteRenderer::isDefaultRoute)
                .findFirst()
                .orElse(null);
    }
}