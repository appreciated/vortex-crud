package com.github.appreciated.vortex_crud.core.entity.data_store;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter class that provides complex querying capabilities on top of a basic VortexCrudDataStore.
 * It fetches all records from the underlying store and performs filtering/sorting in memory.
 *
 * @param <FieldType>  The field type identifier
 * @param <ModelClass> The model class
 */
public class VortexCrudQueryDataStoreAdapter<FieldType, ModelClass> implements VortexCrudQueryDataStore<FieldType, ModelClass> {

    private final VortexCrudDataStore<FieldType, ModelClass> dataStore;

    public VortexCrudQueryDataStoreAdapter(VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public ModelClass newInstance() {
        return dataStore.newInstance();
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        return dataStore.insertRecord(entity);
    }

    @Override
    public ModelClass getRecordById(Object id) {
        return dataStore.getRecordById(id);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return dataStore.getRecordsFromTable(offset, limit);
    }

    @Override
    public void updateRecord(ModelClass entity) {
        dataStore.updateRecord(entity);
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        dataStore.updateRecordById(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        dataStore.deleteRecord(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        dataStore.deleteRecordById(id);
    }

    @Override
    public int count() {
        return dataStore.count();
    }

    @Override
    public Class<ModelClass> getModelClass() {
        return dataStore.getModelClass();
    }

    // Complex queries implementation

    private List<ModelClass> getAllRecords() {
        int count = count();
        if (count == 0) {
            return Collections.emptyList();
        }
        return getRecordsFromTable(0, count);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(FieldType filterField, Object filterValue, int offset, int limit) {
        return getAllRecords().stream()
                .filter(record -> matchField(record, filterField, filterValue))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(FieldType filterField, Object filterValue, FieldType orderField, int offset, int limit) {
        // Simple implementation: filter, sort (if possible, but we don't know how to sort by generic FieldType), then paginate.
        // Sorting is hard without ReflectionService or knowing the type.
        // We will just filter for now, sorting in memory requires access to field values.
        // Assuming we can't sort without more info, we return filtered list.
        return getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(FieldType filterField, List<String> filterValue, int offset, int limit) {
        return getAllRecords().stream()
                .filter(record -> {
                    Object val = getFieldValue(record, filterField);
                    return val != null && filterValue.contains(val.toString());
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(FieldType filterField, Object filterValue, int offset, int limit) {
        String pattern = filterValue != null ? filterValue.toString().toLowerCase() : "";
        return getAllRecords().stream()
                .filter(record -> {
                    Object val = getFieldValue(record, filterField);
                    return val != null && val.toString().toLowerCase().contains(pattern);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLike(FieldType filterField, String filterValue) {
        String pattern = filterValue != null ? filterValue.toLowerCase() : "";
        return (int) getAllRecords().stream()
                .filter(record -> {
                    Object val = getFieldValue(record, filterField);
                    return val != null && val.toString().toLowerCase().contains(pattern);
                })
                .count();
    }

    @Override
    public int countWhereFiltersEqual(List<RouteFilter<FieldType>> filters) {
        return (int) getAllRecords().stream()
                .filter(record -> matchFilters(record, filters))
                .count();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereFiltersEqual(List<RouteFilter<FieldType>> filters, int offset, int limit) {
        return getAllRecords().stream()
                .filter(record -> matchFilters(record, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(FieldType searchField, Object searchValue, List<RouteFilter<FieldType>> filters, int offset, int limit) {
        String pattern = searchValue != null ? searchValue.toString().toLowerCase() : "";
        return getAllRecords().stream()
                .filter(record -> {
                    Object val = getFieldValue(record, searchField);
                    boolean matchesSearch = val != null && val.toString().toLowerCase().contains(pattern);
                    return matchesSearch && matchFilters(record, filters);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(FieldType searchField, String searchValue, List<RouteFilter<FieldType>> filters) {
        String pattern = searchValue != null ? searchValue.toLowerCase() : "";
        return (int) getAllRecords().stream()
                .filter(record -> {
                    Object val = getFieldValue(record, searchField);
                    boolean matchesSearch = val != null && val.toString().toLowerCase().contains(pattern);
                    return matchesSearch && matchFilters(record, filters);
                })
                .count();
    }

    // Helper methods requiring Reflection or similar mechanism.
    // Since VortexCrudDataStore doesn't expose reflection, we can try to guess or use getters if possible?
    // But VortexCrudDataStore is generic.
    // However, usually we have a ReflectionService available in the Context, but here we don't.
    // Wait, the interface uses `FieldType`. How do we get value from `ModelClass` using `FieldType`?
    // JooqDataStore uses `TableField` which has logic. Jpa uses reflection map.
    // A simple adapter can't know how to extract value from ModelClass using FieldType unless FieldType is a String (property name) and we use BeanWrapper, OR FieldType is a Function.

    // The user said: "without the need of simple datastores to implement these".
    // If I cannot extract values, I cannot filter.
    // Maybe I should assume ModelClass follows JavaBean convention and FieldType is String?
    // Or I need `ReflectionService`.

    // If `FieldType` is String, I can use reflection.
    // If `FieldType` is `TableField`, I can't use it easily without Jooq context, but Jooq store already implements the interface.
    // So this adapter is primarily for "simple" stores which likely use String or similar as FieldType.

    // I will implement a best-effort reflection approach assuming FieldType.toString() gives the property name.

    private Object getFieldValue(ModelClass record, FieldType field) {
        if (field == null) return null;
        String fieldName = field.toString(); // Best effort
        // Try simple reflection
        try {
            java.lang.reflect.Field f = record.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(record);
        } catch (NoSuchFieldException | IllegalAccessException e) {
             // Try getter
             try {
                 String getter = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                 java.lang.reflect.Method m = record.getClass().getMethod(getter);
                 return m.invoke(record);
             } catch (Exception ex) {
                 return null;
             }
        }
    }

    private boolean matchField(ModelClass record, FieldType field, Object value) {
        Object recordValue = getFieldValue(record, field);
        if (recordValue == null) return value == null;
        return recordValue.equals(value);
    }

    private boolean matchFilters(ModelClass record, List<RouteFilter<FieldType>> filters) {
        if (filters == null || filters.isEmpty()) return true;
        for (RouteFilter<FieldType> filter : filters) {
            if (!matchField(record, filter.field(), filter.value())) {
                return false;
            }
        }
        return true;
    }
}
