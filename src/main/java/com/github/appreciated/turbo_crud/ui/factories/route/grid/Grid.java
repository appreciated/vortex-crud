package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.ui.components.SearchField;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.components.EntityItemList;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class Grid extends VerticalLayout {
    private final VirtualItemGrid virtualGrid;

    public Grid(TurboCrudPathToRouteResolver routeResolver,
                Route route,
                TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                FormCreator formCreator,
                TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                TurboCrudRouteFactoryRegistry routeFactoryRegistry,
                TurboCrudItemFactoryRegistry itemFactoryRegistry,
                TurboCrudIconFactory iconFactory) {
        RouteHeader routeHeader = new RouteHeader(route, iconFactory);
        String repository = route.getRepository();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, repository, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid(routeResolver, route, entityManagerFactoryRegistry, itemFactoryRegistry);
        add(headerBar, search, virtualGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(false);
    }

    private void applyFilter(String filterText) {
        DataProvider<EntityItemList, ?> dataProvider = virtualGrid.getDataProvider();
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

