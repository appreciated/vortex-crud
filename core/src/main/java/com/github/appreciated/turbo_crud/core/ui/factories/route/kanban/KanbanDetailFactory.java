package com.github.appreciated.turbo_crud.core.ui.factories.route.kanban;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Kanban;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
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
    private final TurboCrudDataStoreFactoryRegistry<DataStoreId,FieldId> dataStoreFactoryRegistry;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final TurboCrudItemFactoryRegistry<FieldId>  turboCrudItemFactory;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public KanbanDetailFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                               TurboCrudConfigService<DataStoreId, FieldId> configService,
                               TurboCrudItemFactoryRegistry<FieldId> turboCrudItemFactory,
                               TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                               FormCreator<DataStoreId, FieldId> formCreator,
                               TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                               TurboCrudFileProviderRegistry fileProviderRegistry,
                               TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.turboCrudItemFactory = turboCrudItemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route<DataStoreId, FieldId> route = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(route.getDataStore(),
                route,
                dataStoreFactoryRegistry.getFactory(route.getDataStore()),
                routeFactory,
                turboCrudItemFactory,
                (Kanban<DataStoreId, FieldId>) route.getConfiguration(),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                fileProviderRegistry,
                fieldNameResolver,
                formCreator,
                detailRouteSetting
               );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
