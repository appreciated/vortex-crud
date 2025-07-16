package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;


/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
public class JooqDataStore<ModelClass extends UpdatableRecord<?>> implements VortexCrudDataStore<TableField<?, ?>, ModelClass> {

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
    public void updateRecord(ModelClass entity) {

    }

    @Override
    public void deleteRecord(ModelClass entity) {

    }

    @Override
    public ModelClass newInstance() {
        try {
            return record.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new instance of " + record.getName(), e);
        }
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
    public void updateRecordById(ModelClass entity) {
        dslContext.executeUpdate(entity);
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
        ModelClass modelInstance = newInstance();
        return modelInstance.getTable();
    }

    @Override
    public Field getField(String foreignKeyField) {
        return null;
    }

}
