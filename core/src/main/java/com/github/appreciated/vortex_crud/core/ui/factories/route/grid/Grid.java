package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.EntityItemList;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public class Grid<DataStoreId, FieldId, ModelClass> extends VerticalLayout {
    private final VirtualItemGrid<DataStoreId, FieldId, ModelClass> virtualGrid;

    public Grid(VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver,
                RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer,
                VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
                VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry,
                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> routeFactoryRegistry,
                VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                VortexCrudFileProviderRegistry fileProviderRegistry,
                VortexCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        DataStoreId dataStore = routeRenderer.getDataStore();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver, routeRenderer, dataStoreFactoryRegistry, itemFactoryRegistry, fileProviderRegistry, resolver);
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

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, ModelClass> dialogFactoryRegistry, RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer, DataStoreId dataStore, FormCreator<DataStoreId, FieldId, ModelClass> formCreator, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass>  routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                null,
                null,
                null,
                routeRenderer.getChild(),
                null,
                dataStore,
                routeFactory,
                () -> {

                },
                formCreator);
        dialog.open();
    }
}

