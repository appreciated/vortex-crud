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

public class KanbanDetailFactory<DataStoreId, FieldId, ModelClass> implements VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>  {
    private final VortexCrudDataStoreFactoryRegistry<DataStoreId,FieldId, ModelClass> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId, ModelClass>  configService;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactory;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory;
    private final FormCreator<DataStoreId, FieldId, ModelClass>  formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass>  dialogFactoryRegistry;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public KanbanDetailFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                               VortexCrudConfigService<DataStoreId, FieldId, ModelClass>  configService,
                               VortexCrudItemFactoryRegistry<FieldId> itemFactory,
                               VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory,
                               FormCreator<DataStoreId, FieldId, ModelClass>  formCreator,
                               VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass>  dialogFactoryRegistry,
                               VortexCrudFileProviderRegistry fileProviderRegistry,
                               VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.itemFactory = itemFactory;
        this.routeFactory = routeFactory;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass>  routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(routeRenderer.getDataStore(),
                routeRenderer,
                dataStoreFactoryRegistry.getDataStore(routeRenderer.getDataStore()),
                routeFactory,
                itemFactory,
                (Kanban<DataStoreId, FieldId, ModelClass> ) routeRenderer.getConfiguration(),
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
