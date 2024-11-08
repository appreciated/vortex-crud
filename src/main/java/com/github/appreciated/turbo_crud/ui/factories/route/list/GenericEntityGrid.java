package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.dataprovider.GenericFilterableDataProvider;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerService;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.DataProvider;

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
        // Set up the data provider with lazy loading and filtering

        Repository tables = configService.getConfiguration().getRepositoriesConfig().get(route.getRepository());
        GridOrListConfiguration gridOrListConfiguration = ConfigBeanFactory.create(route.getConfiguration(), GridOrListConfiguration.class);

        assert gridOrListConfiguration.getFilterField() != null;
        DataProvider<GenericEntity, Void> dataProvider = new GenericFilterableDataProvider(entityManagerService, gridOrListConfiguration.getFilterField()).withConfigurableFilter();

        Map<String, RepositoryField> fieldsConfig = tables.getFieldsConfig();

        // Iterate over the fields defined in the configuration
        for (FormElement field : gridOrListConfiguration.getChildren()) {
            String fieldName = field.getField();
            RepositoryField repositoryField = fieldsConfig.get(fieldName);
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
        getUI().ifPresent(ui -> ui.navigate("/view/" + pathVariables.getPath() + "/" + EntityUtil.getId(entity)));
    }
}