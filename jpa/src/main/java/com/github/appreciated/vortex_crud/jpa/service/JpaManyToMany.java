package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaRepositoryDataStore;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaManyToMany implements ManyToMany<JpaRepository<?, ?>, String> {

    private final String referenceField;

    public JpaManyToMany(String referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<GenericEntity> getData(VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String> dataStoreFactoryRegistry,
                                       String foreignKeyValue,
                                       VortexCrudDataStore<String> dataStore,
                                       CollectionConfiguration<JpaRepository<?, ?>, String> collectionConfiguration) {
        if (foreignKeyValue == null) {
            return List.of();
        }

        try {
            // Get the entity by ID
            GenericEntity entity = dataStore.getRecordById(foreignKeyValue);
            if (entity == null) {
                return List.of();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        return List.of();
    }

    private List<Field> findManyToManyFields(Class<?> entityClass) {
        List<Field> result = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(jakarta.persistence.ManyToMany.class)) {
                result.add(field);
            }
        }
        return result;
    }

    private Object createEntityInstance(Class<?> entityClass, GenericEntity entity) {
        try {
            Object instance = entityClass.getDeclaredConstructor().newInstance();

            // Set ID field
            for (Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    String idFieldName = field.getName();
                    Object idValue = entity.get(idFieldName);
                    if (idValue != null) {
                        field.set(instance, idValue);
                    }
                    break;
                }
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getReferenceField(CollectionConfiguration<JpaRepository<?, ?>, String> collectionConfiguration) {
        return referenceField;
    }

    @Override
    public JpaRepository<?, ?> getAssociativeDataStore() {
        return null; // Not needed for JPA implementation
    }

    @Override
    public String getAssociativeTargetIdField() {
        return ""; // Not needed for JPA implementation
    }
}
