package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.Kanban;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
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

public class KanbanDetailFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {
    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactory;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldId> reflectionService;

    public KanbanDetailFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                               VortexCrudConfigService<DataStoreId, FieldId> configService,
                               VortexCrudItemFactoryRegistry<FieldId> itemFactory,
                               VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                               FormCreator<DataStoreId, FieldId> formCreator,
                               VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                               VortexCrudFileProviderRegistry fileProviderRegistry,
                               VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                               com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldId> reflectionService
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
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<DataStoreId, FieldId>(routeRenderer.getDataStore(),
                routeRenderer,
                dataStoreFactoryRegistry.getDataStore(routeRenderer.getDataStore()),
                routeFactory,
                itemFactory,
                (Kanban<DataStoreId, FieldId>) routeRenderer.getConfiguration(),
                configService.getConfiguration(),
                dialogFactoryRegistry,
                fileProviderRegistry,
                fieldNameResolver,
                formCreator,
                detailRouteSetting,
                reflectionService
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
