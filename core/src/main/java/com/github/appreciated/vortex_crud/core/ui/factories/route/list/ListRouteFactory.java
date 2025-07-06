package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class ListRouteFactory<DataStoreId, FieldId, ModelClass> implements VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService;
    private final VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> columnCallbackRegistry;
    private final FormCreator<DataStoreId, FieldId, ModelClass> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ListRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                            VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService,
                            VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> columnCallbackRegistry,
                            FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                            VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new List<>(currentPathIndex,
                routeResolver,
                dataStoreFactoryRegistry,
                configService,
                columnCallbackRegistry,
                formCreator,
                dialogFactoryRegistry,
                routeFactoryRegistry, fieldNameResolver);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
