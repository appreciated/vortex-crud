package com.github.appreciated.vortex_crud.core.model;

import java.lang.reflect.Field;
import java.util.List;

public interface GenericEntityMapper {

    /**
     * Maps the GenericEntity into a fitting class for the repository using reflection.
     *
     * @param entity The GenericEntity to be mapped.
     * @return The mapped domain-specific object.
     */
     <T> T mapToEntity(GenericEntity entity, Class<T> repositoryModelClass);

     <T> GenericEntity mapFromEntity(T entity, List<Field> fields);
}
