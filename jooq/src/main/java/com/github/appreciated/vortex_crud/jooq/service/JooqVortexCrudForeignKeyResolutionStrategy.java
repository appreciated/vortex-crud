package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation simply sets the foreign key value in the entity using the field name resolver.
 *
 * @param <FieldId> The type of the field identifier
 */
@Service
public class JooqVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<TableField<?, ?>> {



    @Override
    public void resolveForeignKey(GenericEntity entity,
                                  TableField<?, ?> foreignKeyField,
                                  String foreignKeyValue,
                                  VortexCrudDataStore<TableField<?, ?>> dataStore,
                                  VortexCrudDataStoreFieldNameResolver<TableField<?, ?>> fieldNameResolver) {
        if (foreignKeyField != null && foreignKeyValue != null) {
            entity.put(fieldNameResolver.getKeyForFieldId(foreignKeyField), foreignKeyValue);
        }
    }
}