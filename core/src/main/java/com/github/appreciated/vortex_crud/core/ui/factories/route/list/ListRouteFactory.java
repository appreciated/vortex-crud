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

public class ListRouteFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId> columnCallbackRegistry;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ListRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                            VortexCrudConfigService<DataStoreId, FieldId> configService,
                            VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId> columnCallbackRegistry,
                            FormCreator<DataStoreId, FieldId> formCreator,
                            VortexCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
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
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
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
