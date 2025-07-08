package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqDataStoreConfig extends DataStoreConfig<Class<? extends TableRecord<?>>, TableField<?, ?>> {
    public JooqDataStoreConfig(Class<? extends VortexCrudDataStore> factory) {
        super((Class<? extends VortexCrudDataStore<TableField<?, ?>, ?>>) factory);
    }

    public static DataStoreConfig.Builder<Class<? extends TableRecord<?>>, TableField<?, ?>> of(Class<? extends VortexCrudDataStore> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<Class<? extends TableRecord<?>>, TableField<?, ?>>((Class<? extends VortexCrudDataStore<TableField<?, ?>, ?>>) factory));
    }
}
