package com.github.appreciated.vortex_crud.core.ui.factories.route.treegrid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.TreeGridRoute;
import com.github.appreciated.vortex_crud.core.data_provider.GenericHierarchicalDataProvider;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;

import java.util.Map;

public class GenericEntityTreeGrid<ModelClass, FieldType, RepositoryType> extends TreeGrid<Object> {

    private final VortexCrudPathToRouteResolver routeResolver;

    @SuppressWarnings("unchecked")
    public GenericEntityTreeGrid(VortexCrudPathToRouteResolver routeResolver,
                                 TreeGridRoute<ModelClass, FieldType, RepositoryType> routeRenderer,
                                 VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.routeResolver = routeResolver;
        addThemeVariants(GridVariant.LUMO_NO_BORDER);

        DataStoreConfig<ModelClass, FieldType, RepositoryType> tables = routeRenderer.dataStoreConfig();
        RepositoryType table = tables.factory();
        VortexCrudDataStore<FieldType, ModelClass> dataStore = (VortexCrudDataStore<FieldType, ModelClass>) tables.dataStoreInstance();

        // Data Provider
        GenericHierarchicalDataProvider<FieldType> dataProvider = new GenericHierarchicalDataProvider<>(
                dataStore,
                routeRenderer.parentField(),
                routeRenderer.filters(),
                context.dataStoreUtil()
        );

        setDataProvider(dataProvider);

        Map<?, Field<ModelClass, FieldType, RepositoryType>> fieldsConfig = tables.fields();

        // Iterate over the fields defined in the configuration
        for (InternalFormElement<FieldType> field : routeRenderer.columns()) {
            FieldType fieldName = field.field();
            Field<ModelClass, FieldType, RepositoryType> dataStoreField = fieldsConfig.get(fieldName);

            if (fieldName.equals(routeRenderer.titleField())) {
                addHierarchyColumn(item -> context.reflectionService().getString(item, fieldName))
                        .setHeader(getTranslation(field.label()));
            } else {
                context.columnCallbackRegistry().getCallback(routeRenderer).addColumn(this, field, table, dataStoreField);
            }
        }

        setSizeFull();
        addItemClickListener(event -> onItemClick(context, event.getItem()));
    }

    private void onItemClick(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, Object entity) {
        String id = context.dataStoreUtil().getId(entity);
        String nextRoute = routeResolver.buildPathUpToIndex(routeResolver.determineActiveRouteIndex(), id);
        getUI().ifPresent(ui -> ui.navigate(nextRoute));
    }
}
