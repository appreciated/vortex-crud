package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter that turns a simple VortexCrudDataStore into a VortexCrudQueryDataStore
 * by performing filtering and searching in-memory.
 */
public class VortexCrudQueryDataStoreAdapter<FieldType, ModelClass> implements VortexCrudQueryDataStore<FieldType, ModelClass> {

    private final VortexCrudDataStore<FieldType, ModelClass> delegate;
    private final ReflectionService<FieldType> reflectionService;

    public VortexCrudQueryDataStoreAdapter(VortexCrudDataStore<FieldType, ModelClass> delegate, ReflectionService<FieldType> reflectionService) {
        this.delegate = delegate;
        this.reflectionService = reflectionService;
    }

    @Override
    public ModelClass newInstance() {
        return delegate.newInstance();
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        return delegate.insertRecord(entity);
    }

    @Override
    public ModelClass getRecordById(Object id) {
        return delegate.getRecordById(id);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return delegate.getRecordsFromTable(offset, limit);
    }

    @Override
    public void updateRecord(ModelClass entity) {
        delegate.updateRecord(entity);
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        delegate.updateRecordById(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        delegate.deleteRecord(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        delegate.deleteRecordById(id);
    }

    @Override
    public int count() {
        return delegate.count();
    }

    @Override
    public Class<ModelClass> getModelClass() {
        return delegate.getModelClass();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldType filterField, Object filterValue, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    if (val == null) {
                        return filterValue == null;
                    }
                    return val.equals(filterValue);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(FieldType filterField, Object filterValue, FieldType orderField, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    if (val == null) {
                        return filterValue == null;
                    }
                    return val.equals(filterValue);
                })
                .sorted((o1, o2) -> {
                    Object v1 = reflectionService.getValue(o1, orderField);
                    Object v2 = reflectionService.getValue(o2, orderField);

                    if (v1 == null && v2 == null) return 0;
                    if (v1 == null) return -1;
                    if (v2 == null) return 1;

                    if (v1 instanceof Comparable && v2 instanceof Comparable) {
                        try {
                            return ((Comparable) v1).compareTo(v2);
                        } catch (Exception e) {
                            // Fallback to string comparison if types are incompatible
                        }
                    }
                    return String.valueOf(v1).compareTo(String.valueOf(v2));
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(FieldType filterField, List<String> filterValue, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    return val != null && filterValue.contains(val.toString());
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(FieldType filterField, Object filterValue, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    String filterString = filterValue == null ? "" : filterValue.toString().toLowerCase();
                    return val != null && val.toString().toLowerCase().contains(filterString);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLike(FieldType filterField, String filterValue) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return (int) all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, filterField);
                    String filterString = filterValue == null ? "" : filterValue.toLowerCase();
                    return val != null && val.toString().toLowerCase().contains(filterString);
                })
                .count();
    }

    @Override
    public int countWhereFiltersEqual(List<RouteFilter<FieldType>> filters) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return (int) all.stream()
                .filter(item -> matchesFilters(item, filters))
                .count();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereFiltersEqual(List<RouteFilter<FieldType>> filters, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> matchesFilters(item, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(FieldType searchField, Object searchValue, List<RouteFilter<FieldType>> filters, int offset, int limit) {
        List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, searchField);
                    String searchString = searchValue == null ? "" : searchValue.toString().toLowerCase();
                    boolean matchesSearch = val != null && val.toString().toLowerCase().contains(searchString);
                    return matchesSearch && matchesFilters(item, filters);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(FieldType searchField, String searchValue, List<RouteFilter<FieldType>> filters) {
         List<ModelClass> all = delegate.getRecordsFromTable(0, Integer.MAX_VALUE);
        return (int) all.stream()
                .filter(item -> {
                    Object val = reflectionService.getValue(item, searchField);
                    String searchString = searchValue == null ? "" : searchValue.toLowerCase();
                    boolean matchesSearch = val != null && val.toString().toLowerCase().contains(searchString);
                    return matchesSearch && matchesFilters(item, filters);
                })
                .count();
    }

    private boolean matchesFilters(ModelClass item, List<RouteFilter<FieldType>> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        for (RouteFilter<FieldType> filter : filters) {
            Object val = reflectionService.getValue(item, filter.field());
            Object filterVal = filter.value();
            if (val == null) {
                if (filterVal != null) return false;
            } else {
                // If filter value is provided as String but field is not, try to match via toString()
                // This is a naive implementation for adapter purposes.
                if (val.equals(filterVal)) {
                    continue;
                }
                if (filterVal != null && val.toString().equals(filterVal.toString())) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
}
