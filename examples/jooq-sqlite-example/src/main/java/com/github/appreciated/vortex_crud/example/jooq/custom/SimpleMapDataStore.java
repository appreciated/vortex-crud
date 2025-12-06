package com.github.appreciated.vortex_crud.example.jooq.custom;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Simple in-memory Map-based data store demonstrating custom storage backends.
 * Shows how to implement VortexCrudDataStore for non-database storage in jOOQ environment.
 */
public class SimpleMapDataStore implements VortexCrudDataStore<TableField<?, ?>, SimpleMapDataStore.Note> {

    private final Map<Integer, Note> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public static class Note {
        private Integer id;
        private String title;
        private String content;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    @Override
    public Object insertRecord(Note entity) {
        if (entity.getId() == null) {
            entity.setId(idCounter.getAndIncrement());
        }
        store.put(entity.getId(), entity);
        return entity.getId();
    }

    @Override
    public List<Note> getRecordsFromTable(int offset, int limit) {
        return store.values().stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEquals(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEqualsOrdered(TableField<?, ?> filterField, Object filterValue, TableField<?, ?> orderField, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public Note getRecordById(Object id) {
        if (id == null) return null;
        return store.get(Integer.parseInt(id.toString()));
    }

    @Override
    public void updateRecordById(Note entity) {
        if (entity.getId() != null) {
            store.put(entity.getId(), entity);
        }
    }

    @Override
    public void deleteRecordById(Object id) {
        if (id != null) {
            store.remove(Integer.parseInt(id.toString()));
        }
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public int countWhereColumnLike(TableField<?, ?> filterField, String filterValue) {
        return store.size();
    }

    @Override
    public Class<Note> getModelClass() {
        return Note.class;
    }

    @Override
    public void updateRecord(Note entity) {
        updateRecordById(entity);
    }

    @Override
    public void deleteRecord(Note entity) {
        if (entity.getId() != null) {
            deleteRecordById(entity.getId());
        }
    }

    @Override
    public Note newInstance() {
        return new Note();
    }
}
