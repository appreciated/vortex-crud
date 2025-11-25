package com.github.appreciated.vortex_crud.core.ui.factories.route.submenu;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class SubmenuRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public SubmenuRouteFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                               VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.configService = configService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new Submenu<>(currentPathIndex, routeResolver, configService, dataStoreUtil);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
