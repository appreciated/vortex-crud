package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import org.jooq.TableField;

public class JooqDataStoreConfig extends DataStoreConfig<TableField<?,?>> {
    public JooqDataStoreConfig(Class<? extends TurboCrudDataStore> factory) {
        super(factory);
    }

    public static DataStoreConfig.Builder<TableField<?,?>> of(Class<? extends TurboCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<TableField<?,?>>(factory));
    }
}
