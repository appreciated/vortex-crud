package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.*;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.List;
import java.util.Map;

/**
 * A custom Grid component for displaying GenericEntity objects with lazy loading.
 * This grid is configured with a data provider that retrieves and counts records dynamically based on the specified table in the RouteConfig.
 * It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class DefaultListRouteImpl extends Grid<GenericEntity> {

    public DefaultListRouteImpl(int i, RouteConfig routeConfig, DynamicEntityManagerService entityManagerService, FlowCmsConfigService cmsConfigService, FlowCmsListColumnFactory listColumnFactory) {
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

        TableConfig tables = cmsConfigService.getConfiguration().getTablesConfig().get(routeConfig.getTable());
        ItemRendererConfig itemRendererConfig = routeConfig.getRenderConfiguration().getItemRenderer();
        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormField field : itemRendererConfig.getChildren()) {
            String fieldName = field.getColumn();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);
            if (fieldConfig == null) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            listColumnFactory.getListColumn(routeConfig).addColumn(this,field,table,fieldName,fieldConfig);
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
        getUI().ifPresent(ui -> ui.navigate("/view/projects/" + entity.get("id")));
    }
}