package com.github.appreciated.turbo_crud.core.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class GridRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;
    private final TurboCrudItemFactoryRegistry<FieldId> itemFactoryRegistry;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public GridRouteFactory(
            TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
            FormCreator<DataStoreId, FieldId> formCreator,
            TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
            TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
            TurboCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
            TurboCrudFileProviderRegistry fileProviderRegistry,
            TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
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
                fieldNameResolver);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}