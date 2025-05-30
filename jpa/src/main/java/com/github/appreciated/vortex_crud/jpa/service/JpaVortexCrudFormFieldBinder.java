package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.form.VortexCrudFormFieldBinder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the EntityFieldBinder interface.
 */
@Service
public class JpaVortexCrudFormFieldBinder implements VortexCrudFormFieldBinder<String> {

    private final JpaDataStoreFieldNameResolver fieldNameResolver;

    public JpaVortexCrudFormFieldBinder(JpaDataStoreFieldNameResolver fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Object extractEntityForField(GenericEntity entity1, String field) {
        return entity1.get(fieldNameResolver.getKeyForFieldId(field));
    }

    @Override
    public void updateFieldInEntity(GenericEntity entity1, String field, Object o) {
        entity1.put(fieldNameResolver.getKeyForFieldId(field), o);
    }
}
