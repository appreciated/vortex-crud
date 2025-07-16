package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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

public class Grid<DataStoreId, FieldId, KeyType> extends VerticalLayout {
    private final VirtualItemGrid<DataStoreId, FieldId, KeyType> virtualGrid;

    public Grid(VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                VortexCrudFileProviderRegistry fileProviderRegistry,
                VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                ReflectionService<FieldId> reflectionService) {
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, routeRenderer.getDataStoreKey(), formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver,
                routeRenderer,
                dataStoreFactoryRegistry,
                itemFactoryRegistry,
                fileProviderRegistry,
                resolver,
                reflectionService);
        add(headerBar, search, virtualGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(false);
    }

    private void applyFilter(String filterText) {
        DataProvider<EntityItemList<DataStoreId>, ?> dataProvider = virtualGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                       RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                       KeyType dataStore,
                       FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                       VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory) {
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

