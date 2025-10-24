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

public class ListRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry;
    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public ListRouteFactory(VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                            VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry,
                            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                            VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
                            VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
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
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
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
