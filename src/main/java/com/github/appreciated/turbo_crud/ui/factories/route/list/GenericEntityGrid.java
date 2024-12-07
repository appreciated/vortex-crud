package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.turbo_crud.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.data_store.TurboCrudDataStore;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link GenericEntity} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link Route}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid extends Grid<GenericEntity> {

    private final TurboCrudPathToRouteResolver pathVariables;

    public GenericEntityGrid(TurboCrudPathToRouteResolver routeResolver,
                             Route route,
                             TurboCrudDataStoreFactoryRegistry dataStoreFactoryRegistry,
                             TurboCrudConfigService configService,
                             TurboCrudListColumnCallbackRegistry listColumnFactory) {
        this.pathVariables = routeResolver;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        String table = route.getDataStore();
        TurboCrudDataStore dataStore = dataStoreFactoryRegistry.getFactory(table);
        // Set up the data provider with lazy loading and filtering

        DataStore tables = configService.getConfiguration().getRepositories().get(route.getDataStore());
        RouteConfiguration gridOrListConfiguration = route.getConfiguration();

        assert gridOrListConfiguration.getFilterField() != null;
        com.vaadin.flow.data.provider.DataProvider<GenericEntity, Void> dataProvider = new GenericFilterableDataProvider(dataStore, gridOrListConfiguration.getFilterField()).withConfigurableFilter();

        Map<String, Field> fieldsConfig = tables.getFields();

        // Iterate over the fields defined in the configuration
        for (FormElement field : gridOrListConfiguration.getChildren()) {
            String fieldName = field.getField();
            Field repositoryField = fieldsConfig.get(fieldName);
            if (repositoryField == null) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            listColumnFactory.getCallback(route).addColumn(this, field, table, fieldName, repositoryField);
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
    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> ui.navigate("/view/" + pathVariables.getPath() + "/" + DataStoreUtil.getId(entity)));
    }
}