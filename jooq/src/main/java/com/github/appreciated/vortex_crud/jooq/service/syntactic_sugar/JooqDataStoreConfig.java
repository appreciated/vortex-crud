package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.impl.TableImpl;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqDataStoreConfig extends DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public JooqDataStoreConfig(TableImpl<?> factory) {
        super(factory);
    }

    public static DataStoreConfig.Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(TableImpl<?> factory) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>(factory));
    }
}
