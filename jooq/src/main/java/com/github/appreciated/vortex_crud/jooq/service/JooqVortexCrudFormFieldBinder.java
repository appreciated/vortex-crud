package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.form.VortexCrudFormFieldBinder;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the EntityFieldBinder interface.
 */
@Service
public class JooqVortexCrudFormFieldBinder implements VortexCrudFormFieldBinder<TableField<?, ?>> {

    private final JooqDataStoreFieldNameResolver fieldNameResolver;

    public JooqVortexCrudFormFieldBinder(JooqDataStoreFieldNameResolver fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public Object extractEntityForField(GenericEntity entity1, TableField<?, ?> field) {
        return entity1.get(fieldNameResolver.getKeyForFieldId(field));
    }

    @Override
    public void updateFieldInEntity(GenericEntity entity1, TableField<?, ?> field, Object o) {
        entity1.put(fieldNameResolver.getKeyForFieldId(field), o);
    }
}
