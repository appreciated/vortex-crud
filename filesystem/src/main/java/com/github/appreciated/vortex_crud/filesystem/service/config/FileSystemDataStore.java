package com.github.appreciated.vortex_crud.filesystem.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * File system-based implementation of VortexCrudDataStore.
 * Stores entities as JSON files in a local directory.
 * Each entity type gets its own subdirectory, and each entity is stored as {id}.json.
 *
 * @param <ModelClass> The entity class type
 */
public class FileSystemDataStore<ModelClass> implements VortexCrudDataStore<String, ModelClass> {

    private final Class<ModelClass> modelClass;
    private final Path storageDirectory;
    private final ObjectMapper objectMapper;
    private final Field idField;
    private final DataStoreHooks<ModelClass> hooks;
    private final AtomicLong idGenerator;

    public FileSystemDataStore(Class<ModelClass> modelClass,
                               Path storageDirectory,
                               DataStoreHooks<ModelClass> hooks) {
        this.modelClass = modelClass;
        this.storageDirectory = storageDirectory.resolve(modelClass.getSimpleName());
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.idField = findIdField(modelClass);
        this.hooks = hooks != null ? hooks : new DataStoreHooks<>();
        this.idGenerator = new AtomicLong(findMaxId());

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(this.storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: " + this.storageDirectory, e);
        }
    }

    /**
     * Find the maximum ID currently in use to initialize the ID generator
     */
    private long findMaxId() {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".json"))
                    .map(name -> name.substring(0, name.length() - 5))
                    .mapToLong(id -> {
                        try {
                            return Long.parseLong(id);
                        } catch (NumberFormatException e) {
                            return 0L;
                        }
                    })
                    .max()
                    .orElse(0L);
        } catch (IOException e) {
            return 0L;
        }
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        try {
            Object id = getEntityId(entity);

            // Check if this is an update or insert
            if (id != null && getRecordById(id) != null) {
                // Update existing entity
                hooks.beforeUpdates().forEach(hook -> hook.execute(entity));
                writeEntityToFile(entity, id);
                hooks.afterUpdates().forEach(hook -> hook.execute(entity));
            } else {
                // Insert new entity
                if (id == null) {
                    id = generateId();
                    setEntityId(entity, id);
                }
                hooks.beforeCreates().forEach(hook -> hook.execute(entity));
                writeEntityToFile(entity, id);
                hooks.afterCreates().forEach(hook -> hook.execute(entity));
            }

            return id;
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert record", e);
        }
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .sorted(Comparator.comparing(Path::getFileName))
                    .skip(offset)
                    .limit(limit)
                    .map(this::readEntityFromFile)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to list records", e);
        }
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return filterRecords(filterField, filterValue, false, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
        // For simplicity, we'll use the same logic as equals without ordering
        // A more sophisticated implementation could sort by the orderField
        return filterRecords(filterField, filterValue, false, offset, limit);
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .map(this::readEntityFromFile)
                    .filter(Objects::nonNull)
                    .filter(entity -> matchesInFilter(entity, filterField, filterValues))
                    .skip(offset)
                    .limit(limit)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to filter records", e);
        }
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return filterRecords(filterField, filterValue, true, offset, limit);
    }

    @Override
    public ModelClass getRecordById(Object id) {
        Path filePath = getFilePath(id);
        if (!Files.exists(filePath)) {
            return null;
        }
        return readEntityFromFile(filePath);
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        insertRecord(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        hooks.beforeDeletes().forEach(hook -> hook.execute(null));
        Path filePath = getFilePath(id);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete record with ID: " + id, e);
        }
        hooks.afterDeletes().forEach(hook -> hook.execute(null));
    }

    @Override
    public int count() {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return (int) paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .count();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        return (int) filterRecords(filterField, filterValue, true, 0, Integer.MAX_VALUE).size();
    }

    @Override
    public Class<ModelClass> getModelClass() {
        return modelClass;
    }

    @Override
    public void updateRecord(ModelClass entity) {
        insertRecord(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        Object id = getEntityId(entity);
        if (id != null) {
            deleteRecordById(id);
        }
    }

    @Override
    public ModelClass newInstance() {
        try {
            return modelClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Error creating new instance of " + modelClass.getName(), e);
        }
    }

    // Helper methods

    private Field findIdField(Class<?> clazz) {
        // Look for a field named "id" (case-insensitive)
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("id")) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new IllegalArgumentException("No ID field found in class: " + clazz.getName());
    }

    private Object getEntityId(ModelClass entity) {
        try {
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get entity ID", e);
        }
    }

    private void setEntityId(ModelClass entity, Object id) {
        try {
            idField.setAccessible(true);
            // Convert id to the appropriate type
            Object convertedId = convertToFieldType(id, idField.getType());
            idField.set(entity, convertedId);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set entity ID", e);
        }
    }

    private Object generateId() {
        return idGenerator.incrementAndGet();
    }

    private Path getFilePath(Object id) {
        return storageDirectory.resolve(id.toString() + ".json");
    }

    private void writeEntityToFile(ModelClass entity, Object id) {
        Path filePath = getFilePath(id);
        try {
            objectMapper.writeValue(filePath.toFile(), entity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write entity to file: " + filePath, e);
        }
    }

    private ModelClass readEntityFromFile(Path filePath) {
        try {
            return objectMapper.readValue(filePath.toFile(), modelClass);
        } catch (IOException e) {
            System.err.println("Failed to read entity from file: " + filePath + " - " + e.getMessage());
            return null;
        }
    }

    private List<ModelClass> filterRecords(String filterField, Object filterValue, boolean useLike, int offset, int limit) {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .map(this::readEntityFromFile)
                    .filter(Objects::nonNull)
                    .filter(entity -> matchesFilter(entity, filterField, filterValue, useLike))
                    .skip(offset)
                    .limit(limit)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to filter records", e);
        }
    }

    private boolean matchesFilter(ModelClass entity, String filterField, Object filterValue, boolean useLike) {
        try {
            Field field = modelClass.getDeclaredField(filterField);
            field.setAccessible(true);
            Object fieldValue = field.get(entity);

            if (fieldValue == null && filterValue == null) {
                return true;
            }
            if (fieldValue == null || filterValue == null) {
                return false;
            }

            if (useLike) {
                // Case-insensitive contains
                return fieldValue.toString().toLowerCase().contains(filterValue.toString().toLowerCase());
            } else {
                // Exact match (case-insensitive for strings)
                if (fieldValue instanceof String && filterValue instanceof String) {
                    return fieldValue.toString().equalsIgnoreCase(filterValue.toString());
                }
                return fieldValue.equals(filterValue);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    private boolean matchesInFilter(ModelClass entity, String filterField, List<String> filterValues) {
        try {
            Field field = modelClass.getDeclaredField(filterField);
            field.setAccessible(true);
            Object fieldValue = field.get(entity);

            if (fieldValue == null) {
                return false;
            }

            return filterValues.contains(fieldValue.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }

    private Object convertToFieldType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } else if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } else if (targetType == Double.class || targetType == double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return value;
            }
            return Boolean.parseBoolean(value.toString());
        }

        return value;
    }
}
