package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.service.reflection.ReflectionService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Adapter that provides query capabilities on top of a simple VortexCrudDataStore using in-memory filtering.
 *
 * @param <FieldType>  The type used to identify fields
 * @param <ModelClass> The type of entity
 */
public class VortexCrudQueryDataStoreAdapter<FieldType, ModelClass> implements VortexCrudQueryDataStore<FieldType, ModelClass> {

    private final VortexCrudDataStore<FieldType, ModelClass> store;
    private final ReflectionService<FieldType> reflectionService;

    public VortexCrudQueryDataStoreAdapter(VortexCrudDataStore<FieldType, ModelClass> store, ReflectionService<FieldType> reflectionService) {
        this.store = store;
        this.reflectionService = reflectionService;
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldType filterField, Object filterValue, int offset, int limit) {
        return store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> Objects.equals(reflectionService.getValue(item, filterField), filterValue))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(FieldType filterField, Object filterValue, FieldType orderField, int offset, int limit) {
        // Simple implementation: filter then sort (if possible, otherwise just filter)
        // Note: In-memory sorting requires Comparable or Comparator. For now, we just filter.
        // A full implementation would need a way to compare values based on orderField.
        return getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(FieldType filterField, List<String> filterValue, int offset, int limit) {
        return store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    return filterValue.contains(String.valueOf(val));
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(FieldType filterField, Object filterValue, int offset, int limit) {
        String filterStr = String.valueOf(filterValue).toLowerCase();
        return store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    return String.valueOf(val).toLowerCase().contains(filterStr);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLike(FieldType filterField, String filterValue) {
        String filterStr = filterValue.toLowerCase();
        return (int) store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    return String.valueOf(val).toLowerCase().contains(filterStr);
                })
                .count();
    }

    @Override
    public int countWhereFiltersEqual(List<RouteFilter<FieldType>> filters) {
        return (int) store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> applyFilters(item, filters))
                .count();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereFiltersEqual(List<RouteFilter<FieldType>> filters, int offset, int limit) {
        return store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> applyFilters(item, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(FieldType searchField, Object searchValue, List<RouteFilter<FieldType>> filters, int offset, int limit) {
        String filterStr = String.valueOf(searchValue).toLowerCase();
        return store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, searchField);
                    return String.valueOf(val).toLowerCase().contains(filterStr);
                })
                .filter(item -> applyFilters(item, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(FieldType searchField, String searchValue, List<RouteFilter<FieldType>> filters) {
        String filterStr = searchValue.toLowerCase();
        return (int) store.getRecordsFromTable(0, Integer.MAX_VALUE).stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, searchField);
                    return String.valueOf(val).toLowerCase().contains(filterStr);
                })
                .filter(item -> applyFilters(item, filters))
                .count();
    }

    private boolean applyFilters(ModelClass item, List<RouteFilter<FieldType>> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        for (RouteFilter<FieldType> filter : filters) {
            Object val = reflectionService.getValue(item, filter.column());
            if (!Objects.equals(val, filter.equals())) {
                return false;
            }
        }
        return true;
    }

    // Delegate methods from VortexCrudBaseDataStore
    @Override
    public ModelClass newInstance() {
        return store.newInstance();
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        return store.insertRecord(entity);
    }

    @Override
    public ModelClass getRecordById(Object id) {
        return store.getRecordById(id);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return store.getRecordsFromTable(offset, limit);
    }

    @Override
    public void updateRecord(ModelClass entity) {
        store.updateRecord(entity);
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        store.updateRecordById(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        store.deleteRecord(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        store.deleteRecordById(id);
    }

    @Override
    public int count() {
        return store.count();
    }

    @Override
    public Class<ModelClass> getModelClass() {
        return store.getModelClass();
    }
}
