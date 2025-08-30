package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class KanbanDetailFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> {
    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactory;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory;
    private final FormCreator<DataStoreId, FieldId, KeyType> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ReflectionService<FieldId> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public KanbanDetailFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                               VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                               VortexCrudItemFactoryRegistry<FieldId> itemFactory,
                               VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                               FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                               VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                               VortexCrudFileProviderRegistry fileProviderRegistry,
                               VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                               ReflectionService<FieldId> reflectionService,
                               VortexCrudDataStoreUtilStrategy dataStoreUtil

    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.itemFactory = itemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(routeRenderer.getDataStoreKey(),
                routeRenderer,
                dataStoreFactoryRegistry.getDataStore(routeRenderer.getDataStoreKey()),
                routeFactory,
                itemFactory,
                (Kanban<DataStoreId, FieldId, KeyType>) routeRenderer.getConfiguration(),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                fileProviderRegistry,
                fieldNameResolver,
                formCreator,
                detailRouteSetting,
                reflectionService,
                dataStoreUtil,
                routeResolver
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
