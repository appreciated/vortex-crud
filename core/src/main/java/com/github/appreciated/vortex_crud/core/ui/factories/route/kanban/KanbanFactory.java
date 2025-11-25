package com.github.appreciated.vortex_crud.core.ui.factories.route.kanban;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.KanbanConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.component.KanbanView;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class KanbanFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final FormCreator<ModelClass, FieldType, RepositoryType> formCreator;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public KanbanFactory(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                         FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                         VortexCrudFileProviderRegistry fileProviderRegistry,
                         VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                         ReflectionService<FieldType> reflectionService,
                         VortexCrudDataStoreUtilStrategy dataStoreUtil

    ) {
        this.configService = configService;
        this.formCreator = formCreator;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        return new KanbanView<>(routeRenderer.dataStoreKey(),
                routeRenderer,
                configService.configuration().dataStores().get(routeRenderer.dataStoreKey()).dataStoreInstance(),
                (KanbanConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration(),
                configService.configuration(),
                fileProviderRegistry,
                fieldNameResolver,
                formCreator,
                detailRouteSetting,
                reflectionService,
                dataStoreUtil,
                routeResolver
        );
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
