package com.github.appreciated.vortex_crud.jooq.service.reflection;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.jooq.Record;
import org.jooq.TableField;
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
    public <T> Object getValue(T entity, TableField<?, ?> field) {
        if (entity instanceof Record) {
            // Unchecked cast because we know T is compatible with the field if the API is used correctly
            @SuppressWarnings("unchecked")
            Record record = (Record) entity;
            @SuppressWarnings("unchecked")
            TableField<Record, Object> tableField = (TableField<Record, Object>) field;
            return record.get(tableField);
        }
        return null;
    }

    @Override
    public <T> void setValue(T entity, TableField<?, ?> fieldName, Object value) {
        if (entity instanceof Record) {
            @SuppressWarnings("unchecked")
            Record record = (Record) entity;
            @SuppressWarnings("unchecked")
            TableField<Record, Object> tableField = (TableField<Record, Object>) fieldName;
            record.set(tableField, value);
        }
    }

    @Override
    public <T> Object getId(T entity) {
        if (entity instanceof UpdatableRecord) {
            UpdatableRecord<?> record = (UpdatableRecord<?>) entity;
            // key() returns a Record containing the primary key values
            Record keyRecord = record.key();
            if (keyRecord != null && keyRecord.size() > 0) {
                 return keyRecord.get(0);
            }
        }
        return null;
    }

    @Override
    public <T> boolean addAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToAdd) {
        // jOOQ records typically don't hold collections for relationships directly in the record fields
        // unless using custom converters or JSON fields.
        Object value = getValue(entity, fieldName);
        if (value instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) value;
             boolean changed = collection.addAll(elementsToAdd);
             setValue(entity, fieldName, collection);
             return changed;
        }
        return false;
    }

    @Override
    public <T> boolean removeAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToRemove) {
        Object value = getValue(entity, fieldName);
        if (value instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) value;
             boolean changed = collection.removeAll(elementsToRemove);
             setValue(entity, fieldName, collection);
             return changed;
        }
        return false;
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, TableField<?, ?> fieldName) {
        // In jOOQ, field types are strict.
        // fieldName.getType() returns the class of the field.
        Class<?> type = fieldName.getType();
        if (Collection.class.isAssignableFrom(type)) {
            return null;
        }
        return null;
    }
}
