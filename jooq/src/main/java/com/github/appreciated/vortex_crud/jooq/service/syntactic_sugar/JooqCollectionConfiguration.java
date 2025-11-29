package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollectionConfiguration extends CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static CollectionConfiguration.CollectionConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config) {
        return CollectionConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(config);
    }
}