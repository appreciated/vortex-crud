package com.github.appreciated.vortex_crud.example.jooq.custom;

import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Simple in-memory Map-based data store demonstrating custom storage backends.
 * Implements the full VortexCrudDataStore interface with filtering support.
 * <p>
 * This shows how you can plug in ANY storage backend:
 * - File systems
 * - External APIs
 * - NoSQL databases
 * - In-memory structures (like this example)
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
    public Note newInstance() {
        return new Note();
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
    public Note getRecordById(Object id) {
        return store.get(id);
    }

    @Override
    public void updateRecord(Note entity) {
        if (entity.getId() != null) {
            store.put(entity.getId(), entity);
        }
    }

    @Override
    public void updateRecordById(Note entity) {
        updateRecord(entity);
    }

    @Override
    public void deleteRecord(Note entity) {
        if (entity.getId() != null) {
            deleteRecordById(entity.getId());
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
    public Class<Note> getModelClass() {
        return Note.class;
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return store.values().stream()
                .filter(note -> matchesField(note, filterField, filterValue))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
        return getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValue, int offset, int limit) {
        return store.values().stream()
                .filter(note -> {
                    Object value = getFieldValue(note, filterField);
                    return value != null && filterValue.contains(value.toString());
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        String searchPattern = filterValue != null ? filterValue.toString().toLowerCase() : "";
        return store.values().stream()
                .filter(note -> {
                    Object value = getFieldValue(note, filterField);
                    return value != null && value.toString().toLowerCase().contains(searchPattern);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        String searchPattern = filterValue != null ? filterValue.toLowerCase() : "";
        return (int) store.values().stream()
                .filter(note -> {
                    Object value = getFieldValue(note, filterField);
                    return value != null && value.toString().toLowerCase().contains(searchPattern);
                })
                .count();
    }

    @Override
    public int countWhereFiltersEqual(List<RouteFilter<String>> filters) {
        return (int) store.values().stream()
                .filter(note -> matchesFilters(note, filters))
                .count();
    }

    @Override
    public List<Note> getRecordsFromTableWhereFiltersEqual(List<RouteFilter<String>> filters, int offset, int limit) {
        return store.values().stream()
                .filter(note -> matchesFilters(note, filters))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLikeAndFiltersEqual(String searchField, Object searchValue, List<RouteFilter<String>> filters, int offset, int limit) {
        String searchPattern = searchValue != null ? searchValue.toString().toLowerCase() : "";
        return store.values().stream()
                .filter(note -> {
                    Object value = getFieldValue(note, searchField);
                    boolean matchesSearch = value != null && value.toString().toLowerCase().contains(searchPattern);
                    return matchesSearch && matchesFilters(note, filters);
                })
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public int countWhereColumnLikeAndFiltersEqual(String searchField, String searchValue, List<RouteFilter<String>> filters) {
        String searchPattern = searchValue != null ? searchValue.toLowerCase() : "";
        return (int) store.values().stream()
                .filter(note -> {
                    Object value = getFieldValue(note, searchField);
                    boolean matchesSearch = value != null && value.toString().toLowerCase().contains(searchPattern);
                    return matchesSearch && matchesFilters(note, filters);
                })
                .count();
    }

    // Helper methods
    private Object getFieldValue(Note note, String fieldName) {
        return switch (fieldName) {
            case "id" -> note.getId();
            case "title" -> note.getTitle();
            case "content" -> note.getContent();
            default -> null;
        };
    }

    private boolean matchesField(Note note, String fieldName, Object expectedValue) {
        Object value = getFieldValue(note, fieldName);
        return value != null && value.equals(expectedValue);
    }

    private boolean matchesFilters(Note note, List<RouteFilter<String>> filters) {
        if (filters == null || filters.isEmpty()) {
            return true;
        }
        return filters.stream().allMatch(filter ->
            matchesField(note, filter.field(), filter.value())
        );
    }
}
