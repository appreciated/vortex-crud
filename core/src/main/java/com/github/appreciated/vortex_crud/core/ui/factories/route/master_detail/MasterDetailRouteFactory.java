package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class MasterDetailRouteFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ReflectionService<FieldId> reflectionService;

    public MasterDetailRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                    VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                                    VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                                    VortexCrudConfigService<DataStoreId, FieldId> configService,
                                    VortexCrudFileProviderRegistry fileProviderRegistry,
                                    VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                                    ReflectionService<FieldId> reflectionService
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail<>(currentPathIndex,
                routeResolver,
                dataStoreFactoryRegistry,
                itemFactoryRegistry,
                routeFactory,
                configService,
                fileProviderRegistry,
                fieldNameResolver,
                reflectionService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
