package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqDataStoreConfig {
    public static DataStoreConfig.DataStoreConfigBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(TableImpl<?> factory) {
        return DataStoreConfig.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory(factory);
    }
}
