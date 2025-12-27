package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class JooqReflectionService implements ReflectionService<TableField<?, ?>> {

    @Override
    public <T> String getString(T entity, TableField<?, ?> fieldName) {
        Object value = getValue(entity, fieldName);
        return value != null ? value.toString() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Object getValue(T entity, TableField<?, ?> field) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof Record record) {
            return record.get(field);
        }
        throw new IllegalArgumentException("Entity must be an instance of org.jooq.Record");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setValue(T entity, TableField<?, ?> fieldName, Object value) {
        if (entity == null) {
            return;
        }
        if (entity instanceof Record record) {
            ((Record) record).set((TableField) fieldName, value);
        } else {
             throw new IllegalArgumentException("Entity must be an instance of org.jooq.Record");
        }
    }

    @Override
    public <T> Object getId(T entity) {
        if (entity == null) {
            return null;
        }
        if (entity instanceof UpdatableRecord<?> record) {
             Record keyRecord = record.key(); // record.key() returns Record (a copy of PK values)
             if (keyRecord != null && keyRecord.size() > 0) {
                 return keyRecord.get(0);
             }
             return null;
        } else if (entity instanceof Record) {
             // For non-updatable records, we can't reliably determine the ID without metadata.
             return null;
        }
        throw new IllegalArgumentException("Entity must be an instance of org.jooq.UpdatableRecord for ID retrieval");
    }

    @Override
    public <T> boolean addAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToAdd) {
        if (entity == null || elementsToAdd == null || elementsToAdd.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        if (collectionObj == null) {
            collectionObj = new java.util.ArrayList<>();
            setValue(entity, fieldName, collectionObj);
        }

        if (!(collectionObj instanceof Collection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) collectionObj;
        return collection.addAll(elementsToAdd);
    }

    @Override
    public <T> boolean removeAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToRemove) {
        if (entity == null || elementsToRemove == null || elementsToRemove.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        if (collectionObj == null) {
            return false;
        }

        if (!(collectionObj instanceof Collection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) collectionObj;
        return collection.removeAll(elementsToRemove);
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, TableField<?, ?> fieldName) {
        return null;
    }
}
