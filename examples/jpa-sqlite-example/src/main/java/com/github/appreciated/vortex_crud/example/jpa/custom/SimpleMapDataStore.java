package com.github.appreciated.vortex_crud.example.jpa.custom;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Simple in-memory Map-based data store demonstrating custom storage backends.
 * Shows how to implement VortexCrudDataStore for non-database storage.
 */
public class SimpleMapDataStore implements VortexCrudDataStore<String, SimpleMapDataStore.Note> {

    private final Map<Integer, Note> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Data
    public static class Note {
        private Integer id;
        private String title;
        private String content;
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
    public List<Note> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit); // Simplified - no filtering
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public Note getRecordById(Object id) {
        return store.get(id);
    }

    @Override
    public void updateRecordById(Note entity) {
        if (entity.getId() != null) {
            store.put(entity.getId(), entity);
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
    public int countWhereColumnLike(String filterField, String filterValue) {
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
