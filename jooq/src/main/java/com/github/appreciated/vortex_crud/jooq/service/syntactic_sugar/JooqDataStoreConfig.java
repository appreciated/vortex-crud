package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqDataStoreConfig extends DataStoreConfig<TableRecord<?>, TableField<?, ?>> {
    public JooqDataStoreConfig(Class<? extends VortexCrudDataStore> factory) {
        super((Class<? extends VortexCrudDataStore<TableField<?, ?>, ?>>) factory);
    }

    public static DataStoreConfig.Builder<TableRecord<?>, TableField<?, ?>> of(Class<? extends VortexCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<TableRecord<?>, TableField<?, ?>>((Class<? extends VortexCrudDataStore<TableField<?, ?>, ?>>) factory));
    }
}
