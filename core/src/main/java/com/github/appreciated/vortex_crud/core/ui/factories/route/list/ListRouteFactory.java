package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class ListRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public ListRouteFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                            VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry,
                            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                            VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                            VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.formCreator = formCreator;
        this.fieldNameResolver = fieldNameResolver;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new List<>(currentPathIndex,
                routeResolver,
                configService,
                columnCallbackRegistry,
                formCreator,
                fieldNameResolver,
                dataStoreUtil
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
