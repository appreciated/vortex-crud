package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
public class JooqDataStore implements VortexCrudDataStore<TableField<?, ?>> {

    private final DSLContext dslContext;
    private final Class<? extends TableRecord<?>> record;

    public JooqDataStore(Class<? extends TableRecord<?>> record, DSLContext dslContext) {
        this.dslContext = dslContext;
        if (record == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        this.record = record;
    }

    private Class<? extends TableRecord<?>> getRecord() {
        return record;
    }

    @Override
    public Object insertRecord(GenericEntity values) {
        Map<String, Object> properties = values.getProperties();
        try {
            TableRecord<?> o = getRecord().newInstance();
            // Map properties to record
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();
                if (propertyValue != null) {
                    // Convert property name to setter method name (e.g., "name" -> "setName")
                    String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    try {
                        // Find the setter method
                        Method[] methods = o.getClass().getMethods();
                        for (Method method : methods) {
                            if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                                // Call the setter method
                                method.invoke(o, propertyValue);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // Ignore if setter method not found or invocation fails
                    }
                }
            }
            return dslContext.executeInsert(o);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GenericEntity> getRecordsFromTable(int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .map(JooqDataStore::getGenericEntity)
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnEquals(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        if (filterValue == null) {
            return Collections.emptyList();
        }
        return dslContext.select()
                .from(getTable())
                .where(((TableField<?, String>) filterField).eq(filterValue.toString()))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .map(JooqDataStore::getGenericEntity)
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.in(filterValues))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .map(JooqDataStore::getGenericEntity)
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .map(JooqDataStore::getGenericEntity)
                .toList();
    }

    @NotNull
    private static GenericEntity getGenericEntity(Record record) {
        return new GenericEntity(record.intoMap());
    }

    @Override
    public GenericEntity getRecordById(Object id) {
        return dslContext.select()
                .from(getTable())
                .where(DSL.field("id").eq(id))
                .fetchOptionalInto(record)
                .map(JooqDataStore::getGenericEntity)
                .orElse(null);
    }

    @Override
    public void updateRecordById(Object id, GenericEntity values) {
        try {
            // Get the table record class
            TableRecord<?> recordInstance = getRecord().newInstance();
            // Create a map for field names to values
            Map<String, Object> fieldMap = new HashMap<>();

            // Map properties to fields
            for (Map.Entry<String, Object> entry : values.getProperties().entrySet()) {
                String propertyName = entry.getKey();
                Object propertyValue = entry.getValue();
                if (propertyValue != null) {
                    fieldMap.put(propertyName, propertyValue);
                }
            }

            // Execute the update
            dslContext.update(getTable())
                    .set(fieldMap)
                    .where(DSL.field("id").eq(id))
                    .execute();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteRecordById(Object id) {
        dslContext.deleteFrom(getTable())
                .where(DSL.field("id").eq(id))
                .execute();
    }

    @Override
    public void deleteAllRecords() {
        dslContext.deleteFrom(getTable()).execute();
    }

    @Override
    public int count() {
        return dslContext.fetchCount(getTable());
    }

    @Override
    public int countWhereColumnLike(TableField<?, ?> filterField, String filterValue) {
        return dslContext.fetchCount(
                getTable(),
                DSL.field(filterField).like("%" + filterValue + "%")
        );
    }

    @NotNull
    private Table<?> getTable() {
        try {
            return getRecord().newInstance().getTable();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getField(String foreignKeyField) {
        return null;
    }

}
