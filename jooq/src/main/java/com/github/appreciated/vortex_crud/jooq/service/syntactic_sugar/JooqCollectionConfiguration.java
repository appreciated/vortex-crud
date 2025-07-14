package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqCollectionConfiguration extends CollectionConfiguration<TableRecord<?>, TableField<?, ?>> {

    public JooqCollectionConfiguration(Class<TableRecord<?>> factory) {
        super(factory);
    }

    public static class Builder extends CollectionConfiguration.Builder<TableRecord<?>, TableField<?, ?>> {
        public Builder(CollectionConfiguration<TableRecord<?>, TableField<?, ?>> product) {
            super(product);
        }
    }

    public static JooqCollectionConfiguration.Builder of(Class<? extends TableRecord<?>> factory) {
        return new JooqCollectionConfiguration.Builder(new CollectionConfiguration<>(factory));
    }
}