package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.List;
import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link GenericEntity} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link RouteConfig}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid extends Grid<GenericEntity> {

    private final String route;

    public GenericEntityGrid(int i,
                             RouteConfig routeConfig,
                             String route,
                             TurboCrudEntityManagerService entityManagerService,
                             TurboCrudConfigService configService,
                             TurboCrudListColumnCallbackRegistry listColumnFactory) {
        this.route = route;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        String table = routeConfig.getTable();

        // Set up the data provider with lazy loading
        DataProvider<GenericEntity, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Fetch records based on offset and limit
                    List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit());
                    return items.stream();
                },
                query -> entityManagerService.count(table)
        );

        TableConfig tables = configService.getConfiguration().getTablesConfig().get(routeConfig.getTable());
        ItemFactoryConfig itemFactoryConfig = routeConfig.getItems();
        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormElement field : itemFactoryConfig.getChildren()) {
            String fieldName = field.getColumn();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);
            if (fieldConfig == null) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            listColumnFactory.getCallback(routeConfig).addColumn(this,field,table,fieldName,fieldConfig);
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
        getUI().ifPresent(ui -> ui.navigate("/view/" + route + "/" + EntityUtil.getId(entity)));
    }
}