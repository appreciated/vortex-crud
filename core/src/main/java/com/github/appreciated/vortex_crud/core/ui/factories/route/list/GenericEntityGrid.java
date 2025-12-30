package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudQueryDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link Object} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link RouteRenderer}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid<ModelClass, FieldType, RepositoryType> extends Grid<Object> {

    private final  VortexCrudPathToRouteResolver routeResolver;
    private final VortexCrudQueryDataStoreUtilStrategy dataStoreUtil;

    public GenericEntityGrid( VortexCrudPathToRouteResolver routeResolver,
                             RouteRenderer<?, ?, ?> routeRenderer,
                             VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.routeResolver = routeResolver;
        this.dataStoreUtil = context.dataStoreUtil();
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        RouteRenderer<ModelClass, FieldType, RepositoryType> typedRouteRenderer =
                (RouteRenderer<ModelClass, FieldType, RepositoryType>) routeRenderer;
        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = typedRouteRenderer.dataStoreConfig();
        RepositoryType table = tables.factory();
        VortexCrudQueryDataStore<FieldType, ?> dataStore = (VortexCrudQueryDataStore<FieldType, ?>) tables.dataStoreInstance();
        // Set up the data provider with lazy loading and filtering

        com.vaadin.flow.data.provider.DataProvider<Object, Void> dataProvider = new GenericFilterableDataProvider<>(dataStore, typedRouteRenderer.filterField(), typedRouteRenderer.filters()).withConfigurableFilter();

        Map<?, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = tables.fields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> field : typedRouteRenderer.children()) {
            FieldType fieldName = field.field();
            Field<ModelClass, FieldType, RepositoryType> dataStoreField = fieldsConfig.get(fieldName);
            context.columnCallbackRegistry().getCallback(typedRouteRenderer).addColumn(this, field, table, dataStoreField);
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
        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), dataStoreUtil.getId(entity));
        getUI().ifPresent(ui -> ui.navigate(nextRoute));
    }
}
