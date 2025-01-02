package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqDataStoreConfig extends DataStoreConfig<Table<?>, TableField<?,?>> {
    public JooqDataStoreConfig(Class<? extends TurboCrudDataStore> factory) {
        super((Class<? extends TurboCrudDataStore<TableField<?, ?>>>) factory);
    }

    public static DataStoreConfig.Builder<Table<?>, TableField<?,?>> of(Class<? extends TurboCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<Table<?>, TableField<?,?>>((Class<? extends TurboCrudDataStore<TableField<?, ?>>>) factory));
    }
}
