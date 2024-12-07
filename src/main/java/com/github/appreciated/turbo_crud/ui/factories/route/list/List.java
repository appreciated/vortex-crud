package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.ui.components.SearchField;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactory;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
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
        String repository = route.getDataStore();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, repository, formCreator, routeFactoryRegistry),
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

    private void onAdd(TurboCrudDialogFactoryRegistry dialogFactoryRegistry, Route route, String repository, FormCreator formCreator, TurboCrudRouteFactoryRegistry routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory((Class<? extends TurboCrudDialogFactory>) route.getChild().getFactory()).create(
                null,
                null,
                null,
                route.getChild(),
                null,
                repository,
                routeFactory,
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
