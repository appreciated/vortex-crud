package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudQueryDataStoreUtilStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultVortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> implements VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudQueryDataStoreUtilStrategy dataStoreUtil;

    public DefaultVortexCrudPermissionResolutionService(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                                        VortexCrudQueryDataStoreUtilStrategy dataStoreUtil) {
        this.configService = configService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public RouteRenderer<?, ?, ?> resolveRouteForPath(String path) {
        if (configService.configuration() == null) {
            return null;
        }
        Map<String, RouteRenderer<?, ?, ?>> routes = configService.configuration().routes();
        var resolver = new VortexCrudPathToRouteResolver(path, routes, dataStoreUtil);
        return resolver.getCurrentRoute();
    }
}
