package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultVortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> implements VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public DefaultVortexCrudPermissionResolutionService(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                                        VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
                                                        VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public RouteRenderer<ModelClass, FieldType, RepositoryType> resolveRouteForPath(String path) {
        if (configService.configuration() == null) {
            return null;
        }
        Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes = configService.configuration().routes();
        var resolver = new VortexCrudPathToRouteResolver<>(routeFactoryRegistry, path, routes, dataStoreUtil);
        return resolver.getCurrentRoute();
    }
}
