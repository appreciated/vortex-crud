package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.model.GenericEntityMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class JpaGenericEntityMapper implements GenericEntityMapper {

    @Override
    public <T> T mapToEntity(GenericEntity entity, Class<T> repositoryModelClass) {
        try {
            T instance = repositoryModelClass.getDeclaredConstructor().newInstance();
            entity.getProperties().forEach((key, value) -> {
                try {
                    String fieldName = key.substring(0, 1).toLowerCase() + key.substring(1);
                    Field field = repositoryModelClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if (value instanceof GenericEntity) {
                        field.set(instance, mapToEntity((GenericEntity) value, field.getType()));
                    } else {
                        field.set(instance, value);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException("Error mapping field " + key, e);
                } catch (Exception e) {
                    throw e;
                }
            });
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error creating instance of entity class %s".formatted(repositoryModelClass.getName()), e);
        }
    }

    @Override
    public <T> GenericEntity mapFromEntity(T entity, Collection<Field> fields) {
        Map<String, Object> mappingResult = new HashMap<>();
        for (Field field : fields) {
            if (!(field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class))) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null) {
                        // Recursively map complex objects to GenericEntity
                        if (isEntity(value) || value instanceof Collection<?>) {
                            if (value instanceof Collection<?>) {
                                // Handle collections of objects
                                Collection<?> collection = (Collection<?>) value;
                                List<Object> mappedCollection = new ArrayList<>();
                                for (Object item : collection) {
                                    if (isEntity(item)) {
                                        // Recursively map each item in the collection
                                        mappedCollection.add(mapFromEntity(item, Arrays.asList(item.getClass().getDeclaredFields())));
                                    } else {
                                        mappedCollection.add(item);
                                    }
                                }
                                mappingResult.put(field.getName(), mappedCollection);
                            } else {
                                // Handle single complex object
                                mappingResult.put(field.getName(), mapFromEntity(value, Arrays.asList(value.getClass().getDeclaredFields())));
                            }
                        } else {
                            // Simple value, store directly
                            mappingResult.put(field.getName(), value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return new GenericEntity(mappingResult);
    }

    private static boolean isEntity(Object value) {
        return value.getClass().isAnnotationPresent(Entity.class);
    }
}
