package com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class MasterDetailRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final TurboCrudItemFactoryRegistry<FieldId> itemFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> resolver;

    public MasterDetailRouteFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                    TurboCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                                    TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                                    TurboCrudConfigService<DataStoreId, FieldId> configService,
                                    TurboCrudFileProviderRegistry fileProviderRegistry,
                                    TurboCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.resolver = resolver;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail<>(currentPathIndex, routeResolver, dataStoreFactoryRegistry, itemFactoryRegistry, routeFactory, configService, fileProviderRegistry, resolver);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
