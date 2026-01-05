package com.github.appreciated.vortex_crud.core.ui.factories.route.view;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.CustomViewFactoryRoute;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class CustomViewFactoryRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component renderRoute(
            VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
            Integer currentPathIndex,
            VortexCrudPathToRouteResolver routeResolver,
            @Nullable DetailRouteSetting detailRouteSetting
    ) {
        CustomViewFactoryRoute<ModelClass, FieldType, RepositoryType> routeRenderer =
                (CustomViewFactoryRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = routeRenderer.dataStoreConfig();
        VortexCrudDataStore<FieldType, ModelClass> dataStore = tables.dataStoreInstance();

        ModelClass entity;
        boolean creationMode = detailRouteSetting != null && detailRouteSetting.isCreationMode();

        if (creationMode) {
            entity = dataStore.newInstance();
        } else {
            // Traditional mode: fetch by ID from URL path
            String lastSegment = routeResolver.getLastSegment();
            entity = dataStore.getRecordById(lastSegment);
        }

        return routeRenderer.viewFactory().create(entity);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
