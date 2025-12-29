package com.github.appreciated.vortex_crud.example.jooq.custom;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudBaseDataStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Simple in-memory Map-based data store demonstrating custom storage backends.
 * Uses the simplified VortexCrudBaseDataStore interface - perfect for custom implementations!
 * <p>
 * This shows how you can plug in ANY storage backend:
 * - File systems
 * - External APIs
 * - NoSQL databases
 * - In-memory structures (like this example)
 */
public class SimpleMapDataStore implements VortexCrudBaseDataStore<SimpleMapDataStore.Note> {

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
}
