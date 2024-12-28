package com.github.appreciated.turbo_crud.core.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Kanban;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class KanbanDetailFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {
    private final TurboCrudDataStoreFactoryRegistry<DataStoreId> dataStoreFactoryRegistry;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final TurboCrudItemFactoryRegistry turboCrudItemFactory;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final FormCreator formCreator;
    private final TurboCrudDialogFactoryRegistry dialogFactoryRegistry;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;

    public KanbanDetailFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId> dataStoreFactoryRegistry,
                               TurboCrudConfigService<DataStoreId, FieldId> configService,
                               TurboCrudItemFactoryRegistry turboCrudItemFactory,
                               TurboCrudRouteFactoryRegistry routeFactory,
                               FormCreator formCreator,
                               TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                               TurboCrudFileProviderRegistry fileProviderRegistry
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route<DataStoreId, FieldId> route = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView(route.getDataStore(),
                route,
                dataStoreFactoryRegistry.getFactory(route.getDataStore()),
                routeFactory,
                turboCrudItemFactory,
                (Kanban) route.getConfiguration(),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                fileProviderRegistry,
                formCreator,
                detailRouteSetting);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
