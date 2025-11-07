package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollectionConfiguration extends CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static CollectionConfiguration.CollectionConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(TableImpl<?> factory) {
        return CollectionConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStore(factory);
    }
}