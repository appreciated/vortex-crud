package com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class MasterDetailRouteFactory implements TurboCrudRouteFactory {

    private final TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public MasterDetailRouteFactory(TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                                    TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                    TurboCrudRouteFactoryRegistry routeFactory,
                                    TurboCrudConfigService configService,
                                    TurboCrudFileProviderRegistry fileProviderRegistry
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail(currentPathIndex, routeResolver, dataStoreFactoryRegistry, itemFactoryRegistry, routeFactory, configService, fileProviderRegistry);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
