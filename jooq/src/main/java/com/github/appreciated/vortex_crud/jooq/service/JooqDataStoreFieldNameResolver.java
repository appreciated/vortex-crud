package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

@Service
public class JooqDataStoreFieldNameResolver implements VortexCrudQueryDataStoreFieldNameResolver<TableField<?, ?>> {

    @Override
    public String getKeyForFieldType(TableField<?, ?> tableField) {
        return tableField.getName();
    }

}
