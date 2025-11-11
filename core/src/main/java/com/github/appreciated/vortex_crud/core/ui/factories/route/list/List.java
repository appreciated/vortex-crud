package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.CustomRouteActionContext;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.HashSet;

public class List<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final GenericEntityGrid<ModelClass, FieldType, RepositoryType> entityGrid;

    public List(Integer currentPathIndex,
                VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> columnCallbackRegistry,
                FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
                VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactoryRegistry,
                VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer = (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RepositoryType dataStore = routeRenderer.dataStoreKey();

        // Create action context for custom actions
        CustomRouteActionContext<ModelClass> actionContext = CustomRouteActionContext.<ModelClass>builder()
                .dataStore(dataStoreFactoryRegistry.getDataStore(dataStore))
                .selectedEntities(new HashSet<>())  // List doesn't support selection yet
                .refreshCallback(() -> UI.getCurrent().getPage().reload())
                .viewComponent(this)
                .build();

        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader,
                routeRenderer.routeActions(),
                actionContext);
        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid<>(routeResolver, routeRenderer, dataStoreFactoryRegistry, configService, columnCallbackRegistry, dataStoreUtil);
        add(headerBar, textField, entityGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void applyFilter(String filterText) {
        DataProvider<Object, ?> dataProvider = entityGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry, RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer, RepositoryType dataStore, FormCreator<ModelClass, FieldType, RepositoryType> formCreator, VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.child().factory()).create(
                null,
                null,
                null,
                routeRenderer.child(),
                null,
                dataStore,
                routeFactory,
                () -> UI.getCurrent().getPage().reload(),
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
