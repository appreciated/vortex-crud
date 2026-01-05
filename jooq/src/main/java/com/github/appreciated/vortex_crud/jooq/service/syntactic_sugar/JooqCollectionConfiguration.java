package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Collection;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollectionConfiguration {
    public static Collection.CollectionBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config) {
        return Collection.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config);
    }
}
