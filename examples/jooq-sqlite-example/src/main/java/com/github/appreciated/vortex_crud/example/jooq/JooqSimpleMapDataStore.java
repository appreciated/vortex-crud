package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JooqSimpleMapDataStore implements VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> {

    public static class NotesTable extends TableImpl<TableRecord<?>> {
        public static final NotesTable NOTES = new NotesTable();
        public final TableField<TableRecord<?>, Integer> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.INTEGER);
        public final TableField<TableRecord<?>, String> TITLE = createField(DSL.name("title"), org.jooq.impl.SQLDataType.VARCHAR);
        public final TableField<TableRecord<?>, String> CONTENT = createField(DSL.name("content"), org.jooq.impl.SQLDataType.VARCHAR);

        public NotesTable() {
            super(DSL.name("notes"));
        }
    }

    private final Map<Integer, TableRecord<?>> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final DSLContext dsl;

    public JooqSimpleMapDataStore(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Object insertRecord(TableRecord<?> entity) {
        Integer id = entity.get(NotesTable.NOTES.ID);
        if (id == null) {
            id = idCounter.getAndIncrement();
            entity.set(NotesTable.NOTES.ID, id);
        }
        store.put(id, entity);
        return id;
    }

    @Override
    public List<TableRecord<?>> getRecordsFromTable(int offset, int limit) {
        return store.values().stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<TableRecord<?>> getRecordsFromTableWhereColumnEquals(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<TableRecord<?>> getRecordsFromTableWhereColumnEqualsOrdered(TableField<?, ?> filterField, Object filterValue, TableField<?, ?> orderField, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<TableRecord<?>> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<TableRecord<?>> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public TableRecord<?> getRecordById(Object id) {
        return store.get(id);
    }

    @Override
    public void updateRecordById(TableRecord<?> entity) {
        Integer id = entity.get(NotesTable.NOTES.ID);
        if (id != null) {
            store.put(id, entity);
        }
    }

    @Override
    public void deleteRecordById(Object id) {
        store.remove(id);
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public int countWhereColumnLike(TableField<?, ?> filterField, String filterValue) {
        return store.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<TableRecord<?>> getModelClass() {
        return (Class<TableRecord<?>>) (Class<?>) TableRecord.class;
    }

    @Override
    public void updateRecord(TableRecord<?> entity) {
        updateRecordById(entity);
    }

    @Override
    public void deleteRecord(TableRecord<?> entity) {
        Integer id = entity.get(NotesTable.NOTES.ID);
        if (id != null) {
            deleteRecordById(id);
        }
    }

    @Override
    public TableRecord<?> newInstance() {
        return dsl.newRecord(NotesTable.NOTES);
    }
}
