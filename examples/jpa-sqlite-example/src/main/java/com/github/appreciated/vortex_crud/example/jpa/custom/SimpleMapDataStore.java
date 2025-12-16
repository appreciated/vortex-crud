package com.github.appreciated.vortex_crud.example.jpa.custom;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

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
    public List<Note> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return store.values().stream()
                .filter(note -> Objects.equals(getFieldValue(note, filterField), filterValue))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
        return store.values().stream()
                .filter(note -> Objects.equals(getFieldValue(note, filterField), filterValue))
                .sorted((o1, o2) -> {
                    Comparable v1 = (Comparable) getFieldValue(o1, orderField);
                    Comparable v2 = (Comparable) getFieldValue(o2, orderField);
                    if (v1 == null && v2 == null) return 0;
                    if (v1 == null) return -1;
                    if (v2 == null) return 1;
                    return v1.compareTo(v2);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Object getFieldValue(Note note, String fieldName) {
        try {
            java.lang.reflect.Field field = Note.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(note);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return store.values().stream()
                .filter(note -> filterValues.contains(String.valueOf(getFieldValue(note, filterField))))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return store.values().stream()
                .filter(note -> String.valueOf(getFieldValue(note, filterField)).contains(filterValue.toString()))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
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
        return (int) store.values().stream()
                .filter(note -> String.valueOf(getFieldValue(note, filterField)).contains(filterValue))
                .count();
    }

    @Override
    public int countWhereFiltersEqual(java.util.List<com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String>> filters) {
        return (int) store.values().stream()
                .filter(note -> matchesFilters(note, filters))
                .count();
    }

    @Override
    public List<Note> getRecordsFromTableWhereFiltersEqual(java.util.List<com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String>> filters, int offset, int limit) {
        return store.values().stream()
                .filter(note -> matchesFilters(note, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLikeAndFiltersEqual(String searchField, Object searchValue, java.util.List<com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String>> filters, int offset, int limit) {
        return store.values().stream()
                .filter(note -> String.valueOf(getFieldValue(note, searchField)).contains(searchValue.toString()))
                .filter(note -> matchesFilters(note, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(String searchField, String searchValue, java.util.List<com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String>> filters) {
        return (int) store.values().stream()
                .filter(note -> String.valueOf(getFieldValue(note, searchField)).contains(searchValue))
                .filter(note -> matchesFilters(note, filters))
                .count();
    }

    private boolean matchesFilters(Note note, java.util.List<com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String>> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        for (com.github.appreciated.vortex_crud.core.config.model.DefaultFilter<String> filter : filters) {
            if (!Objects.equals(getFieldValue(note, filter.field()), filter.value())) {
                return false;
            }
        }
        return true;
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
