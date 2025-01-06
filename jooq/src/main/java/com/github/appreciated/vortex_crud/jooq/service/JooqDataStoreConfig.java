package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqDataStoreConfig extends DataStoreConfig<Table<?>, TableField<?,?>> {
    public JooqDataStoreConfig(Class<? extends VortexCrudDataStore> factory) {
        super((Class<? extends VortexCrudDataStore<TableField<?, ?>>>) factory);
    }

    public static DataStoreConfig.Builder<Table<?>, TableField<?,?>> of(Class<? extends VortexCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<Table<?>, TableField<?,?>>((Class<? extends VortexCrudDataStore<TableField<?, ?>>>) factory));
    }
}
