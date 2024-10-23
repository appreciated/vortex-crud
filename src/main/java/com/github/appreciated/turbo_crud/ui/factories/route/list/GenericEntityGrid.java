package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.FieldConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.typesafe.config.Config;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.List;
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
                             TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                             TurboCrudConfigService configService,
                             TurboCrudListColumnCallbackRegistry listColumnFactory) {
        this.pathVariables = routeResolver;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        String table = route.getRepository();
        TurboCrudEntityManagerService entityManagerService = entityManagerFactoryRegistry.getFactory(table);
        // Set up the data provider with lazy loading
        DataProvider<GenericEntity, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    // Fetch records based on offset and limit
                    List<GenericEntity> items = entityManagerService.getRecordsFromTable(query.getOffset(), query.getLimit());
                    return items.stream();
                },
                query -> entityManagerService.count()
        );

        RepositoryConfig tables = configService.getConfiguration().getRepositoriesConfig().get(route.getRepository());
        Config itemFactoryConfig = route.getConfiguration();
        Map<String, FieldConfig> fieldsConfig = tables.getFieldsConfig();

        /*
        // Iterate over the fields defined in the configuration
        for (FormElement field : itemFactoryConfig.getString()) {
            String fieldName = field.getColumn();
            FieldConfig fieldConfig = fieldsConfig.get(fieldName);
            if (fieldConfig == null) {
                throw new IllegalStateException("Field '" + fieldName + "' not found in the config unter table '" + table + "'");
            }
            listColumnFactory.getCallback(pathElement).addColumn(this,field,table,fieldName,fieldConfig);
        }*/

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
        getUI().ifPresent(ui -> ui.navigate("/view/" + pathVariables.getPath() + "/" + EntityUtil.getId(entity)));
    }
}