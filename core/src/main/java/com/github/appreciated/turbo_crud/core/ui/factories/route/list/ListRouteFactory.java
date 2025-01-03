package com.github.appreciated.turbo_crud.core.ui.factories.route.list;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class ListRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final TurboCrudListColumnCallbackRegistry<DataStoreId, FieldId> columnCallbackRegistry;
    private final FormCreator<DataStoreId, FieldId> formCreator;
    private final TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ListRouteFactory(TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                            TurboCrudConfigService<DataStoreId, FieldId> configService,
                            TurboCrudListColumnCallbackRegistry<DataStoreId, FieldId> columnCallbackRegistry,
                            FormCreator<DataStoreId, FieldId> formCreator,
                            TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                            TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
                            TurboCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
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
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
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
