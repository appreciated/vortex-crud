package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollectionConfiguration extends CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public JooqCollectionConfiguration(TableImpl<?> factory) {
        super(factory);
    }

    public static class Builder extends CollectionConfiguration.Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
        public Builder(CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> product) {
            super(product);
        }
    }

    public static JooqCollectionConfiguration.Builder of(TableImpl<?> factory) {
        return new JooqCollectionConfiguration.Builder(new CollectionConfiguration<>(factory));
    }
}