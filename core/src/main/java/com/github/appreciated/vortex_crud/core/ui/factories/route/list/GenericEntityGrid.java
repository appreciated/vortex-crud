package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.SignalService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.shared.Registration;

import java.util.Map;

/**
 * A custom {@link Grid} component for displaying {@link Object} objects with lazy loading. This grid is
 * configured with a data provider that retrieves and counts records dynamically based on the specified table in the
 * {@link RouteRenderer}. It also supports click events for rows to navigate to a detailed view of each entity.
 */

public class GenericEntityGrid<ModelClass, FieldType, RepositoryType> extends Grid<Object> {

    private final VortexCrudPathToRouteResolver routeResolver;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final SignalService signalService;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private Registration signalRegistration;

    public GenericEntityGrid(VortexCrudPathToRouteResolver routeResolver,
                             ListRoute<?, ?, ?> routeRenderer,
                             VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.routeResolver = routeResolver;
        this.dataStoreUtil = context.dataStoreUtil();
        this.signalService = context.signalService();
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
        ListRoute<ModelClass, FieldType, RepositoryType> typedRouteRenderer =
                (ListRoute<ModelClass, FieldType, RepositoryType>) routeRenderer;
        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = typedRouteRenderer.dataStoreConfig();
        RepositoryType table = tables.factory();
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) tables.dataStoreInstance();
        // Set up the data provider with lazy loading and filtering

        com.vaadin.flow.data.provider.DataProvider<Object, Void> dataProvider = new GenericFilterableDataProvider<>(dataStore, typedRouteRenderer.filterField(), typedRouteRenderer.filters()).withConfigurableFilter();

        Map<?, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = tables.fields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<ModelClass, FieldType, RepositoryType> field : typedRouteRenderer.columns()) {
            FieldType fieldName = field.field();
            Field<ModelClass, FieldType, RepositoryType> dataStoreField = fieldsConfig.get(fieldName);
            context.columnCallbackRegistry().getCallback(typedRouteRenderer).addColumn(this, field, table, dataStoreField);
        }

        setDataProvider(dataProvider);
        setSizeFull();
        addItemClickListener(event -> onItemClick(event.getItem()));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if (signalService != null && dataStore != null) {
            String topic = "entity-change:" + dataStore.getModelClass().getName();
            signalRegistration = signalService.subscribe(topic, signal -> {
                getUI().ifPresent(ui -> ui.access(this.getDataProvider()::refreshAll));
            });
        }
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        if (signalRegistration != null) {
            signalRegistration.remove();
        }
    }

    /**
     * Handles click events for each entity row, navigating to the detailed view based on the entity's ID.
     *
     * @param entity the clicked Object
     */
    private void onItemClick(Object entity) {
        String id = dataStoreUtil.getId(entity);
        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), id);
        getUI().ifPresent(ui -> ui.navigate(nextRoute));
    }
}
