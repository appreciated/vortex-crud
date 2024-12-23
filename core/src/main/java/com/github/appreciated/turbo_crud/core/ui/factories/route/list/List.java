package com.github.appreciated.turbo_crud.core.ui.factories.route.list;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.core.ui.components.SearchField;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class List extends VerticalLayout {
    private final GenericEntityGrid entityGrid;

    public List(Integer currentPathIndex,
                TurboCrudPathToRouteResolver routeResolver,
                TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                TurboCrudConfigService configService,
                TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                FormCreator formCreator,
                TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                TurboCrudRouteFactoryRegistry routeFactoryRegistry) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(route);
        String dataStore = route.getDataStore();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);
        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid(routeResolver, route, dataStoreFactoryRegistry, configService, columnCallbackRegistry);
        add(headerBar, textField, entityGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(false);
    }

    private void applyFilter(String filterText) {
        DataProvider<GenericEntity, ?> dataProvider = entityGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(TurboCrudDialogFactoryRegistry dialogFactoryRegistry, Route route, String dataStore, FormCreator formCreator, TurboCrudRouteFactoryRegistry routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(route.getChild().getFactory()).create(
                null,
                null,
                null,
                route.getChild(),
                null,
                dataStore,
                routeFactory,
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
