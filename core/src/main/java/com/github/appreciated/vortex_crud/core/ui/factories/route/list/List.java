package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class List<DataStoreId, FieldId, ModelClass> extends VerticalLayout {
    private final GenericEntityGrid<DataStoreId, FieldId, ModelClass> entityGrid;

    public List(Integer currentPathIndex,
                    VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver,
                    VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                    VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService,
                    VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> columnCallbackRegistry,
                    FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                    VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry,
                    VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                    VortexCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        DataStoreId dataStore = routeRenderer.getDataStore();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);
        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid<>(routeResolver, routeRenderer, dataStoreFactoryRegistry, configService, columnCallbackRegistry, resolver);
        add(headerBar, textField, entityGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(false);
    }

    private void applyFilter(String filterText) {
        DataProvider<ModelClass, ?> dataProvider = entityGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry, RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer, DataStoreId dataStore, FormCreator<DataStoreId, FieldId, ModelClass> formCreator, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                null,
                null,
                null,
                routeRenderer.getChild(),
                null,
                dataStore,
                routeFactory,
                () -> {
                    System.out.println();
                },
                formCreator);
        dialog.open();
    }
}
