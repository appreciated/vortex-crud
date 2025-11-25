package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class MasterDetailRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public MasterDetailRouteFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                    VortexCrudFileProviderRegistry fileProviderRegistry,
                                    VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                                    ReflectionService<FieldType> reflectionService,
                                    VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail<>(currentPathIndex,
                routeResolver,
                configService,
                fileProviderRegistry,
                fieldNameResolver,
                reflectionService,
                dataStoreUtil
        );
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
