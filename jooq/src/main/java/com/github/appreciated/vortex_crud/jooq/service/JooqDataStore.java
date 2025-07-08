package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
public class JooqDataStore<ModelClass extends TableRecord<?>> implements VortexCrudDataStore<TableField<?, ?>, ModelClass> {

    private final DSLContext dslContext;
    private final Class<ModelClass> record;

    public JooqDataStore(Class<ModelClass> record, DSLContext dslContext) {
        this.dslContext = dslContext;
        if (record == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        this.record = record;
    }

    public Class<ModelClass> getModelClass() {
        return record;
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        return dslContext.executeInsert(entity);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
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
                .toList();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.in(filterValues))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    public ModelClass getRecordById(Object id) {
        return dslContext.select()
                .from(getTable())
                .where(DSL.field("id").eq(id))
                .fetchOptionalInto(record)
                .orElse(null);
    }

    @Override
    public void updateRecordById(ModelClass values) {
        try {
            // Get the table record class
            TableRecord<?> recordInstance = getModelClass().newInstance();
            // Create a map for field names to values
            Map<String, ModelClass> fieldMap = new HashMap<>();

            // Map properties to fields
            for (Map.Entry<String, ModelClass> entry : values.getProperties().entrySet()) {
                String propertyName = entry.getKey();
                ModelClass propertyValue = entry.getValue();
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
        ModelClass modelInstance = createModelInstance();
        return modelInstance.getTable();
    }

    @Override
    public Field getField(String foreignKeyField) {
        return null;
    }

}
