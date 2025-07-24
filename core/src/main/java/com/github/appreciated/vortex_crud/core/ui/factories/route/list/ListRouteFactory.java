package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
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

public class ListRouteFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> {

    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, KeyType> columnCallbackRegistry;
    private final FormCreator<DataStoreId, FieldId, KeyType> formCreator;
    private final VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public ListRouteFactory(VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                            VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                            VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, KeyType> columnCallbackRegistry,
                            FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                            VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver,
                            VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.dataStoreFactoryRegistry = dataStoreFactoryRegistry;
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.formCreator = formCreator;
        this.dialogFactoryRegistry = dialogFactoryRegistry;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new List<>(currentPathIndex,
                routeResolver,
                dataStoreFactoryRegistry,
                configService,
                columnCallbackRegistry,
                formCreator,
                dialogFactoryRegistry,
                routeFactoryRegistry,
                fieldNameResolver,
                dataStoreUtil
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
