package com.github.appreciated.vortex_crud.example.jooq.custom;

import com.github.appreciated.vortex_crud.core.entity.DateRange;
import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleMapDataStore<K> implements VortexCrudDataStore<K, SimpleMapDataStore.Note> {

    private final Map<Integer, Note> store = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);
    private final Function<K, String> nameExtractor;

    public SimpleMapDataStore(Function<K, String> nameExtractor) {
        this.nameExtractor = nameExtractor;
        // Add some initial data
        Note note = new Note();
        note.setId(idCounter.getAndIncrement());
        note.setTitle("Welcome Note");
        note.setContent("This is a sample note showing various field types.");
        note.setPrice(new BigDecimal("99.99"));
        note.setActive(true);
        store.put(note.getId(), note);
    }

    public static class Note {
        private Integer id;
        private String title;
        private String content;
        private BigDecimal price;
        private Boolean active;
        private DateRange projectDuration;
        private DateTimeRange eventDuration;
        private String attachment;
        private String markdownContent;
        private List<String> tags;
        private String document;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public DateRange getProjectDuration() { return projectDuration; }
        public void setProjectDuration(DateRange projectDuration) { this.projectDuration = projectDuration; }
        public DateTimeRange getEventDuration() { return eventDuration; }
        public void setEventDuration(DateTimeRange eventDuration) { this.eventDuration = eventDuration; }
        public String getAttachment() { return attachment; }
        public void setAttachment(String attachment) { this.attachment = attachment; }
        public String getMarkdownContent() { return markdownContent; }
        public void setMarkdownContent(String markdownContent) { this.markdownContent = markdownContent; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
        public String getDocument() { return document; }
        public void setDocument(String document) { this.document = document; }
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
        return store.values().stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEquals(K filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnEqualsOrdered(K filterField, Object filterValue, K orderField, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnIn(K filterField, List<String> filterValues, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Note> getRecordsFromTableWhereColumnLike(K filterField, Object filterValue, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public Note getRecordById(Object id) {
        if (id instanceof Integer) return store.get(id);
        try {
            return store.get(Integer.parseInt(id.toString()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void updateRecordById(Note entity) {
        if (entity.getId() != null) {
            store.put(entity.getId(), entity);
        }
    }

    @Override
    public void deleteRecordById(Object id) {
        if (id instanceof Integer) store.remove(id);
        else {
            try {
                store.remove(Integer.parseInt(id.toString()));
            } catch (Exception e) {}
        }
    }

    @Override
    public int count() {
        return store.size();
    }

    @Override
    public int countWhereColumnLike(K filterField, String filterValue) {
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
