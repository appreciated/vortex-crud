package com.github.appreciated.vortex_crud.test.jooq.ui.custom;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Specialized data store for managing actual files in a directory.
 * Maps files to Document entities and provides read/write access.
 *
 * This demonstrates how to create a custom data store that works with
 * actual files rather than just storing entities as JSON.
 */
public class FileDocumentDataStore implements VortexCrudDataStore<String, Document> {

    private final Path documentsDirectory;
    private final DataStoreHooks<Document> hooks;
    private final Map<Long, String> idToFileNameMap = new HashMap<>();
    private long nextId = 1;

    /**
     * Create a FileDocumentDataStore without hooks.
     * This is the recommended constructor for simple use cases.
     *
     * @param documentsDirectory The directory containing the documents
     */
    public FileDocumentDataStore(Path documentsDirectory) {
        this(documentsDirectory, new DataStoreHooks<>());
    }

    /**
     * Create a FileDocumentDataStore with custom hooks.
     * Use this constructor if you need to add lifecycle hooks.
     *
     * @param documentsDirectory The directory containing the documents
     * @param hooks Lifecycle hooks (optional)
     */
    public FileDocumentDataStore(Path documentsDirectory, DataStoreHooks<Document> hooks) {
        this.documentsDirectory = documentsDirectory;
        this.hooks = hooks != null ? hooks : new DataStoreHooks<>();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(documentsDirectory);
            indexExistingFiles();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create documents directory: " + documentsDirectory, e);
        }
    }

    /**
     * Index existing files in the directory and assign IDs
     */
    private void indexExistingFiles() throws IOException {
        try (Stream<Path> paths = Files.list(documentsDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        idToFileNameMap.put(nextId++, fileName);
                    });
        }
    }

    @Override
    public Object insertRecord(Document entity) {
        try {
            if (entity.getId() != null && idToFileNameMap.containsKey(entity.getId())) {
                // Update existing file
                hooks.beforeUpdates().forEach(hook -> hook.execute(entity));
                writeDocumentToFile(entity);
                hooks.afterUpdates().forEach(hook -> hook.execute(entity));
                return entity.getId();
            } else {
                // Create new file
                if (entity.getId() == null) {
                    entity.setId(nextId++);
                }
                hooks.beforeCreates().forEach(hook -> hook.execute(entity));

                // Ensure fileName is set
                if (entity.getFileName() == null) {
                    entity.setFileName(entity.getTitle().replaceAll("[^a-zA-Z0-9-_]", "_") + ".txt");
                }

                idToFileNameMap.put(entity.getId(), entity.getFileName());
                writeDocumentToFile(entity);
                hooks.afterCreates().forEach(hook -> hook.execute(entity));
                return entity.getId();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert document", e);
        }
    }

    @Override
    public List<Document> getRecordsFromTable(int offset, int limit) {
        try (Stream<Path> paths = Files.list(documentsDirectory)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .skip(offset)
                    .limit(limit)
                    .map(this::readDocumentFromFile)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to list documents", e);
        }
    }

    @Override
    public List<Document> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return filterDocuments(filterField, filterValue, false, offset, limit);
    }

    @Override
    public List<Document> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
        return filterDocuments(filterField, filterValue, false, offset, limit);
    }

    @Override
    public List<Document> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return getRecordsFromTable(offset, limit);
    }

    @Override
    public List<Document> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return filterDocuments(filterField, filterValue, true, offset, limit);
    }

    @Override
    public Document getRecordById(Object id) {
        String fileName = idToFileNameMap.get(((Number) id).longValue());
        if (fileName == null) {
            return null;
        }
        Path filePath = documentsDirectory.resolve(fileName);
        if (!Files.exists(filePath)) {
            return null;
        }
        return readDocumentFromFile(filePath);
    }

    @Override
    public void updateRecordById(Document entity) {
        insertRecord(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        hooks.beforeDeletes().forEach(hook -> hook.execute(null));
        String fileName = idToFileNameMap.get(((Number) id).longValue());
        if (fileName != null) {
            Path filePath = documentsDirectory.resolve(fileName);
            try {
                Files.deleteIfExists(filePath);
                idToFileNameMap.remove(((Number) id).longValue());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete document with ID: " + id, e);
            }
        }
        hooks.afterDeletes().forEach(hook -> hook.execute(null));
    }

    @Override
    public int count() {
        try (Stream<Path> paths = Files.list(documentsDirectory)) {
            return (int) paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .count();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        return filterDocuments(filterField, filterValue, true, 0, Integer.MAX_VALUE).size();
    }

    @Override
    public Class<Document> getModelClass() {
        return Document.class;
    }

    @Override
    public void updateRecord(Document entity) {
        insertRecord(entity);
    }

    @Override
    public void deleteRecord(Document entity) {
        if (entity.getId() != null) {
            deleteRecordById(entity.getId());
        }
    }

    @Override
    public Document newInstance() {
        return new Document();
    }

    // Helper methods

    private void writeDocumentToFile(Document document) {
        Path filePath = documentsDirectory.resolve(document.getFileName());
        try {
            Files.writeString(filePath, document.getContent());
            document.setModifiedAt(LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write document to file: " + filePath, e);
        }
    }

    private Document readDocumentFromFile(Path filePath) {
        try {
            String fileName = filePath.getFileName().toString();
            String content = Files.readString(filePath);

            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);

            Document doc = new Document();
            // Find ID for this file
            Long id = idToFileNameMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(fileName))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            doc.setId(id);
            doc.setFileName(fileName);
            doc.setTitle(fileName.replace(".txt", "").replace("_", " "));
            doc.setContent(content);
            doc.setFileSize(attrs.size());
            doc.setCreatedAt(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
            doc.setModifiedAt(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));

            return doc;
        } catch (IOException e) {
            System.err.println("Failed to read document from file: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    private List<Document> filterDocuments(String filterField, Object filterValue, boolean useLike, int offset, int limit) {
        try (Stream<Path> paths = Files.list(documentsDirectory)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .map(this::readDocumentFromFile)
                    .filter(Objects::nonNull)
                    .filter(doc -> matchesFilter(doc, filterField, filterValue, useLike))
                    .skip(offset)
                    .limit(limit)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to filter documents", e);
        }
    }

    private boolean matchesFilter(Document document, String filterField, Object filterValue, boolean useLike) {
        if (filterValue == null) {
            return true;
        }

        String fieldValue = switch (filterField) {
            case "fileName" -> document.getFileName();
            case "title" -> document.getTitle();
            case "content" -> document.getContent();
            default -> null;
        };

        if (fieldValue == null) {
            return false;
        }

        if (useLike) {
            return fieldValue.toLowerCase().contains(filterValue.toString().toLowerCase());
        } else {
            return fieldValue.equalsIgnoreCase(filterValue.toString());
        }
    }
}
