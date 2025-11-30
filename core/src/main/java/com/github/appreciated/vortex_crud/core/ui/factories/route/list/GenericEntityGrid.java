package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
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

    private final VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;

    public GenericEntityGrid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                             RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer,
                             VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.routeResolver = routeResolver;
        this.context = context;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        RepositoryType table = routeRenderer.dataStoreKey();
        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = context.configService().configuration().dataStores().get(routeRenderer.dataStoreKey());
        VortexCrudDataStore<FieldType, ?> dataStore = tables.dataStoreInstance();
        // Set up the data provider with lazy loading and filtering
        @SuppressWarnings("unchecked")
        RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> gridOrListConfiguration =
                routeRenderer.configuration();

        com.vaadin.flow.data.provider.DataProvider<Object, Void> dataProvider = new GenericFilterableDataProvider<>(dataStore, gridOrListConfiguration != null ? gridOrListConfiguration.filterField() : null).withConfigurableFilter();

        Map<?, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = tables.fields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> field : gridOrListConfiguration.children()) {
            FieldType fieldName = field.field();
            Field<ModelClass, FieldType, RepositoryType> dataStoreField = fieldsConfig.get(fieldName);
            context.listColumnCallbackRegistry().getCallback(routeRenderer).addColumn(this, field, table, dataStoreField);
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
        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), context.dataStoreUtil().getId(entity));
        getUI().ifPresent(ui -> ui.navigate(nextRoute));
    }
}