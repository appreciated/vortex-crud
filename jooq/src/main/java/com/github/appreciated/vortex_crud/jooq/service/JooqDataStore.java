package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
public class JooqDataStore implements VortexCrudDataStore<TableField<?, ?>> {

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
        org.jooq.Field<?>[] fields = properties.keySet().stream()
                .map(DSL::field)
                .toArray(org.jooq.Field[]::new);

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
                .map(JooqDataStore::getGenericEntity);
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
                .fetch()
                .map(JooqDataStore::getGenericEntity);
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.in(filterValues))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(JooqDataStore::getGenericEntity);
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(JooqDataStore::getGenericEntity);
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
                .fetchOptional()
                .map(JooqDataStore::getGenericEntity)
                .orElse(null);
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
        return dslContext.fetchCount(getTable());
    }

    @Override
    public int countWhereColumnLike(TableField<?, ?> filterField, String filterValue) {
        return dslContext.fetchCount(
                table,
                DSL.field(filterField).like("%" + filterValue + "%")
        );
    }

}
