package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation simply sets the foreign key value in the entity using the field name resolver.
 */
@Service
public class JooqVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<TableField<?, ?>> {

    private final ReflectionService<TableField<?, ?>> reflectionService;

    public JooqVortexCrudForeignKeyResolutionStrategy(ReflectionService<TableField<?, ?>> reflectionService) {
        this.reflectionService = reflectionService;
    }

    @Override
    public void resolveForeignKey(Object entity, TableField<?, ?> foreignKeyField, Object foreignKeyValue, VortexCrudQueryDataStore<TableField<?, ?>, ?> dataStore, VortexCrudQueryDataStoreFieldNameResolver<TableField<?, ?>> fieldNameResolver) {
        if (foreignKeyField != null && foreignKeyValue != null) {
            reflectionService.setValue(entity, foreignKeyField, foreignKeyValue);
        }
    }
}