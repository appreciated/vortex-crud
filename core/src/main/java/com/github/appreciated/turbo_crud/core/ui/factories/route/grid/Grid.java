package com.github.appreciated.turbo_crud.core.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.turbo_crud.core.ui.components.SearchField;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.components.EntityItemList;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class Grid<DataStoreId, FieldId> extends VerticalLayout {
    private final VirtualItemGrid<DataStoreId, FieldId> virtualGrid;

    public Grid(TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                    Route<DataStoreId, FieldId> route,
                    TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> turboCrudDataStoreFactoryRegistry,
                    FormCreator<DataStoreId, FieldId> formCreator,
                    TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                    TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry,
                    TurboCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                    TurboCrudFileProviderRegistry fileProviderRegistry,
                    TurboCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        RouteHeader routeHeader = new RouteHeader(route);
        DataStoreId dataStore = route.getDataStore();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, route, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver, route, turboCrudDataStoreFactoryRegistry, itemFactoryRegistry, fileProviderRegistry, resolver);
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

    private void onAdd(TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry, Route<DataStoreId, FieldId> route, DataStoreId dataStore, FormCreator<DataStoreId, FieldId> formCreator, TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory) {
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

