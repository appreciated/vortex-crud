package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

@Service
public class JooqDataStoreFieldNameResolver implements TurboCrudDataStoreFieldNameResolver<TableField<?, ?>> {

    @Override
    public String getKeyForFieldId(TableField<?, ?> tableField) {
        return tableField.getName();
    }
}
