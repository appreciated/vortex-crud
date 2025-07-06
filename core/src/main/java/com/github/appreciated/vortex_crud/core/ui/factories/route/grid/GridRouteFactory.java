package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class GridRouteFactory<DataStoreId, FieldId, ModelClass>  implements VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>  {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass>  dataStoreFactoryRegistry;
    private final FormCreator<DataStoreId, FieldId, ModelClass>  formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass>  dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass>  routeFactoryRegistry;
    private final VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public GridRouteFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass>  dataStoreFactoryRegistry,
            FormCreator<DataStoreId, FieldId, ModelClass>  formCreator,
            VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass>  dialogFactoryRegistry,
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass>  routeFactoryRegistry,
            VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
            VortexCrudFileProviderRegistry fileProviderRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
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
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass>  routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {

        RouteRenderer<DataStoreId, FieldId, ModelClass>  routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

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