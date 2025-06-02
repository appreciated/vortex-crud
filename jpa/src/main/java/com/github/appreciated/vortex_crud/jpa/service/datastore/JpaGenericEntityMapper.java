package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.model.GenericEntityMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            try {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    mappingResult.put(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            catch (Exception e) {
                throw e;
            }
        }
        return new GenericEntity(mappingResult);
    }
}
