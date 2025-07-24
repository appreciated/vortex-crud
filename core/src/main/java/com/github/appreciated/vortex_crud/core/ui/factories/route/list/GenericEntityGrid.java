package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link Object} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link RouteRenderer}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid<DataStoreId, FieldId, KeyType> extends Grid<Object> {

    private final VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> pathVariables;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public GenericEntityGrid(VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                             RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer,
                             VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                             VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                             VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, KeyType> listColumnFactory,
                             VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.pathVariables = routeResolver;
        this.dataStoreUtil = dataStoreUtil;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        KeyType table = routeRenderer.getDataStoreKey();
        VortexCrudDataStore<FieldId, ?> dataStore = dataStoreFactoryRegistry.getDataStore(table);
        // Set up the data provider with lazy loading and filtering

        DataStoreConfig<DataStoreId, FieldId, KeyType> tables = configService.getConfiguration().getDataStores().get(routeRenderer.getDataStoreKey());
        RouteRendererConfiguration<DataStoreId, FieldId, KeyType> gridOrListConfiguration = routeRenderer.getConfiguration();

        assert gridOrListConfiguration.getFilterField() != null;
        com.vaadin.flow.data.provider.DataProvider<Object, Void> dataProvider = new GenericFilterableDataProvider<>(dataStore, gridOrListConfiguration.getFilterField()).withConfigurableFilter();

        Map<?, Field<DataStoreId, FieldId, KeyType>> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId, KeyType> field : gridOrListConfiguration.getChildren()) {
            FieldId fieldName = field.getField();
            Field<DataStoreId, FieldId, KeyType> dataStoreField = fieldsConfig.get(fieldName);
            listColumnFactory.getCallback(routeRenderer).addColumn(this, field, table, dataStoreField);
        }

        setDataProvider(dataProvider);
        setSizeFull();
        addItemClickListener(event -> onItemClick(event.getItem()));
    }

    /**
     * Handles click events for each entity row, navigating to the detailed view based on the entity's ID.
     *
     * @param entity the clicked Object
     */
    private void onItemClick(Object entity) {
        String v = pathVariables.getPath() + "/" + dataStoreUtil.getId(entity);
        getUI().ifPresent(ui -> ui.navigate(v));
    }
}