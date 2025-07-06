package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link ModelClass} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link RouteRenderer}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid<DataStoreId, FieldId, ModelClass> extends Grid<ModelClass> {

    private final VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> pathVariables;

    public GenericEntityGrid(VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver,
                             RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer,
                             VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
                             VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService,
                             VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, ModelClass> listColumnFactory,
                             VortexCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.pathVariables = routeResolver;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        DataStoreId table = routeRenderer.getDataStore();
        VortexCrudDataStore<FieldId, ModelClass> dataStore = dataStoreFactoryRegistry.getDataStore(table);
        // Set up the data provider with lazy loading and filtering

        DataStoreConfig<DataStoreId, FieldId, ModelClass> tables = configService.getConfiguration().getDataStores().get(routeRenderer.getDataStore());
        RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> gridOrListConfiguration = routeRenderer.getConfiguration();

        assert gridOrListConfiguration.getFilterField() != null;
        com.vaadin.flow.data.provider.DataProvider<ModelClass, Void> dataProvider = new GenericFilterableDataProvider<>(dataStore, gridOrListConfiguration.getFilterField()).withConfigurableFilter();

        Map<?, Field<DataStoreId, FieldId, ModelClass>> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<DataStoreId, FieldId, ModelClass> field : gridOrListConfiguration.getChildren()) {
            FieldId fieldName = field.getField();
            Field<DataStoreId, FieldId, ModelClass> dataStoreField = fieldsConfig.get(fieldName);
            if (dataStoreField == null) {
                throw new IllegalStateException("Field '" + resolver.getKeyForFieldId(fieldName) + "' not found in the config unter table '" + table + "'");
            }
            listColumnFactory.getCallback(routeRenderer).addColumn(this, field, table, resolver.getKeyForFieldId(fieldName), dataStoreField);
        }

        setDataProvider(dataProvider);
        setSizeFull();
        addItemClickListener(event -> onItemClick(event.getItem()));
    }

    /**
     * Handles click events for each entity row, navigating to the detailed view based on the entity's ID.
     *
     * @param entity the clicked GenericEntity
     */
    private void onItemClick(ModelClass entity) {
        String v = pathVariables.getPath() + "/" + DataStoreUtil.getId(entity);
        getUI().ifPresent(ui -> ui.navigate(v));
    }
}