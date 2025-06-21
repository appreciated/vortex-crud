package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import org.jooq.TableField;
import org.jooq.TableRecord;

public class JooqCollectionConfiguration extends CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> {

    public JooqCollectionConfiguration(Class<? extends TableRecord<?>> factory) {
        super(factory);
    }

    public static class Builder extends CollectionConfiguration.Builder<Class<? extends TableRecord<?>>, TableField<?, ?>> {
        public Builder(CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> product) {
            super(product);
        }
    }

    public static JooqCollectionConfiguration.Builder of(Class<? extends TableRecord<?>> factory) {
        return new JooqCollectionConfiguration.Builder(new CollectionConfiguration<>(factory));
    }
}