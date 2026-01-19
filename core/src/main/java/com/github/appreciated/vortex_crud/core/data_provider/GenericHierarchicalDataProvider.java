package com.github.appreciated.vortex_crud.core.data_provider;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.config.model.StaticRouteFilter;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.vaadin.flow.data.provider.hierarchy.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GenericHierarchicalDataProvider<FieldType> extends AbstractBackEndHierarchicalDataProvider<Object, String> {

    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final FieldType parentField;
    private final VortexCrudDataStoreUtilStrategy utilStrategy;
    private final List<RouteFilter<FieldType>> baseFilters;

    public GenericHierarchicalDataProvider(VortexCrudDataStore<FieldType, ?> dataStore,
                                           FieldType parentField,
                                           List<RouteFilter<FieldType>> baseFilters,
                                           VortexCrudDataStoreUtilStrategy utilStrategy) {
        this.dataStore = dataStore;
        this.parentField = parentField;
        this.baseFilters = baseFilters;
        this.utilStrategy = utilStrategy;
    }

    @Override
    protected Stream<Object> fetchChildrenFromBackEnd(HierarchicalQuery<Object, String> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        List<RouteFilter<FieldType>> filters = new ArrayList<>();
        if (baseFilters != null) {
            filters.addAll(baseFilters);
        }

        Object parent = query.getParent();
        Object parentId = null;
        if (parent != null) {
             parentId = utilStrategy.getId(parent);
        }

        filters.add(new StaticRouteFilter<>(parentField, parentId));

        return dataStore.getRecordsFromTableWhereFiltersEqual(filters, offset, limit).stream().map(obj -> (Object) obj);
    }

    @Override
    public int getChildCount(HierarchicalQuery<Object, String> query) {
        List<RouteFilter<FieldType>> filters = new ArrayList<>();
        if (baseFilters != null) {
            filters.addAll(baseFilters);
        }

        Object parent = query.getParent();
        Object parentId = null;
        if (parent != null) {
            parentId = utilStrategy.getId(parent);
        }

        filters.add(new StaticRouteFilter<>(parentField, parentId));

        return dataStore.countWhereFiltersEqual(filters);
    }

    @Override
    public boolean hasChildren(Object item) {
        // We can optimize this if needed, but for now re-using getChildCount for simplicity
        return getChildCount(new HierarchicalQuery<>(null, item)) > 0;
    }
}
