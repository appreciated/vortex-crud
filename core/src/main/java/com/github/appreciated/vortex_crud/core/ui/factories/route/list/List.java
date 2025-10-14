package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
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

public class List<DataStoreId, FieldId, KeyType> extends VerticalLayout {
    private final GenericEntityGrid<DataStoreId, FieldId, KeyType> entityGrid;

    public List(Integer currentPathIndex,
                VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, KeyType> columnCallbackRegistry,
                FormCreator<DataStoreId, FieldId, KeyType> formCreator,
                VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        KeyType dataStore = routeRenderer.getDataStoreKey();
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(dialogFactoryRegistry, routeRenderer, dataStore, formCreator, routeFactoryRegistry),
                null,
                null,
                routeHeader);
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

    private void onAdd(VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry, RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer, KeyType dataStore, FormCreator<DataStoreId, FieldId, KeyType> formCreator, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory) {
        Dialog dialog = dialogFactoryRegistry.getFactory(routeRenderer.getChild().getFactory()).create(
                null,
                null,
                null,
                routeRenderer.getChild(),
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
