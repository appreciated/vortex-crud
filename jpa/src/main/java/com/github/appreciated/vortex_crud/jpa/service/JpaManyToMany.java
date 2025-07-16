package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA implementation of the ManyToMany interface.
 * Uses reflection to work with model classes directly.
 */
public class JpaManyToMany<ModelClass> implements ManyToMany<ModelClass, String, JpaRepository<?,?>> {

    private final String referenceField;

    public JpaManyToMany(String referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public <ModelClass1> List<ModelClass1> getData(VortexCrudDataStoreFactoryRegistry<ModelClass, String, JpaRepository<?,?>> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<String, ModelClass1> dataStore, CollectionConfiguration<ModelClass, String, JpaRepository<?,?>> collectionConfiguration) {
        if (foreignKeyValue == null) {
            return List.of();
        }

        try {
            // Get the entity by ID using reflection
            Object entity = dataStore.getRecordById(foreignKeyValue);
            if (entity == null) {
                return List.of();
            }

            // Implementation will use reflection to work with the model class
            // This is a stub implementation that returns an empty list

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of();
    }

    @Override
    public String getReferenceField(CollectionConfiguration<ModelClass, String, JpaRepository<?,?>> collectionConfiguration) {
        return "";
    }

    /**
     * Finds all fields in the entity class that are annotated with @ManyToMany.
     *
     * @param entityClass The entity class to search
     * @return A list of fields with @ManyToMany annotation
     */
    private List<Field> findManyToManyFields(Class<?> entityClass) {
        List<Field> result = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(jakarta.persistence.ManyToMany.class)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * Creates a new instance of the entity class and sets its ID field.
     *
     * @param entityClass The entity class to instantiate
     * @param id          The ID value to set
     * @return A new instance of the entity class with ID set
     */
    private <T> T createEntityInstance(Class<T> entityClass, Object id) {
        try {
            T instance = entityClass.getDeclaredConstructor().newInstance();

            // Set ID field
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    field.set(instance, id);
                    break;
                }
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a property value from an object using reflection.
     *
     * @param obj          The object to get the property from
     * @param propertyName The name of the property
     * @return The property value
     */
    private Object getPropertyValue(Object obj, String propertyName) {
        try {
            // Try to use getter method first
            String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Method getter = obj.getClass().getMethod(getterName);
                return getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                // Fall back to direct field access
                Field field = obj.getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getReferenceField() {
        return referenceField;
    }

    @Override
    public ModelClass getAssociativeDataStore() {
        return null; // Not needed for JPA implementation
    }

    @Override
    public String getAssociativeTargetIdField() {
        return ""; // Not needed for JPA implementation
    }

    @Override
    public String getAssociativeSourceIdField() {
        return "";
    }
}
