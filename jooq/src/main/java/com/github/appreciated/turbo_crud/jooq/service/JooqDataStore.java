package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;

import org.jooq.Record;

import java.util.List;

/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Map;

public class JooqDataStore implements TurboCrudDataStore<TableField<?,?>> {

    private final DSLContext dslContext;
    private final Table<?> table;

    public JooqDataStore(Table<?> table, DSLContext dslContext) {
        this.dslContext = dslContext;
        if (table == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        this.table = table;
    }

    private Table<?> getTable() {
        return table;
    }

    @Override
    public Object insertRecord(GenericEntity values) {
        Map<String, Object> properties = values.getProperties();
        Field<Object>[] fields = properties.keySet().stream()
                .map(DSL::field)
                .toArray(Field[]::new);

        return dslContext.insertInto(getTable())
                .columns(fields)
                .values(properties.values())
                .returning(DSL.field("id")) // Assuming "id" is the primary key
                .fetchOne()
                .getValue(DSL.field("id"));
    }

    @Override
    public List<GenericEntity> getRecordsFromTable(int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(record -> new GenericEntity(record.intoMap()));
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnEquals(TableField<?,?> filterField, String filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(((TableField<?,String>)filterField).eq(filterValue))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(record -> new GenericEntity(record.intoMap()));
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnIn(TableField<?,?> filterField, List<String> filterValues, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.in(filterValues))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(record -> new GenericEntity(record.intoMap()));
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(TableField<?,?> filterField, String filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(record -> new GenericEntity(record.intoMap()));
    }

    @Override
    public GenericEntity getRecordById(Object id) {
        Record record = dslContext.select()
                .from(getTable())
                .where(DSL.field("id").eq(id))
                .fetchOne();

        return record != null ? new GenericEntity(record.intoMap()) : null;
    }

    @Override
    public void updateRecordById(Object id, GenericEntity values) {
        dslContext.update(getTable())
                .set(values.getProperties())
                .where(DSL.field("id").eq(id))
                .execute();
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
        return dslContext.selectCount()
                .from(getTable())
                .fetchOne(0, int.class);
    }

    @Override
    public int countWhereColumnLike(TableField<?,?> filterField, String filterValue) {
        return dslContext.selectCount()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .fetchOne(0, int.class);
    }
}
