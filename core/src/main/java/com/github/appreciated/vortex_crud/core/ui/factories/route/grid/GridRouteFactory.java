package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class GridRouteFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final ReflectionService reflectionService;

    public GridRouteFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
            FormCreator<DataStoreId, FieldId> formCreator,
            VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
            VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
            VortexCrudFileProviderRegistry fileProviderRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
            ReflectionService reflectionService
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {

        RouteRenderer<DataStoreId, FieldId> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new Grid<>(routeResolver,
                routeRenderer,
                dataStoreFactoryRegistry,
                formCreator,
                dialogFactoryRegistry,
                routeFactoryRegistry,
                itemFactoryRegistry,
                fileProviderRegistry,
                fieldNameResolver,
                reflectionService);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}