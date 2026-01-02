package com.github.appreciated.vortex_crud.jooq.service.reflection;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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
        if (entity instanceof UpdatableRecord) {
            return ((UpdatableRecord<?>) entity).get(field);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setValue(T entity, TableField<?, ?> fieldName, Object value) {
        if (entity instanceof UpdatableRecord) {
            ((UpdatableRecord<?>) entity).set((TableField) fieldName, value);
        }
    }

    @Override
    public <T> Object getId(T entity) {
        if (entity instanceof UpdatableRecord) {
            UpdatableRecord<?> record = (UpdatableRecord<?>) entity;
            // Assuming single primary key
            if (record.key() != null && record.key().size() > 0) {
                return record.key().get(0);
            }
        }
        return null;
    }

    @Override
    public <T> boolean addAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToAdd) {
        // JOOQ records typically map 1:1 to table rows. Collections are usually not stored directly in the record
        // unless using array types or similar.
        // Assuming this method is not applicable or we handle it if the field type is a collection.
        // But Standard SQL fields are not collections.
        // If we are using converters for list/set, the value IS a collection.

        Object collectionObj = getValue(entity, fieldName);

        if (collectionObj == null) {
            // Cannot instantiate a collection without knowing the type or if the field supports it.
            // But if we assume it's a field mapped to a collection (e.g. JSON or Array)
            // For now, let's skip initialization if null, or assume ArrayList if we can't determine.
            // But better to just check if it is a collection.
             return false;
        }

        if (collectionObj instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) collectionObj;
             // We modify the collection. Does JOOQ track this change?
             // If the collection object itself is modified, JOOQ might not know the record changed if we don't call set().
             boolean changed = collection.addAll(elementsToAdd);
             if (changed) {
                 setValue(entity, fieldName, collection);
             }
             return changed;
        }
        return false;
    }

    @Override
    public <T> boolean removeAll(T entity, TableField<?, ?> fieldName, Collection<?> elementsToRemove) {
        Object collectionObj = getValue(entity, fieldName);
        if (collectionObj instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) collectionObj;
             boolean changed = collection.removeAll(elementsToRemove);
             if (changed) {
                 setValue(entity, fieldName, collection);
             }
             return changed;
        }
        return false;
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, TableField<?, ?> fieldName) {
         // JOOQ fields have DataType.
         // If it is an array or converted type, we might be able to determine it.
         // For now return null or try to inspect the field type.
         if (fieldName != null) {
             Class<?> type = fieldName.getType();
             if (Collection.class.isAssignableFrom(type)) {
                 // Determining generic type of the collection from Class<?> is hard/impossible due to type erasure
                 // unless we have extra metadata.
                 return null;
             }
         }
         return null;
    }
}
