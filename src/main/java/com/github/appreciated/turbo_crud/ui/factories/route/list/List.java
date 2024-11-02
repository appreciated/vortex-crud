package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.ui.components.SearchField;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class List extends VerticalLayout {
    private final GenericEntityGrid entityGrid;

    public List(Integer currentPathIndex,
                TurboCrudPathToRouteResolver routeResolver,
                TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                TurboCrudConfigService configService,
                TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                FormCreator formCreator,
                TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                TurboCrudIconFactory iconFactory) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(route, iconFactory);
        String repository = route.getRepository();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, repository, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);
        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid(routeResolver, route, entityManagerFactoryRegistry, configService, columnCallbackRegistry);
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
        Dialog dialog = dialogFactoryRegistry.getFactory(route.getChild().getFactory()).createDialog(
                null,
                null,
                null,
                route.getChild(),
                repository,
                routeFactory,
                () -> {

                },
                formCreator);
        dialog.open();
    }
}
