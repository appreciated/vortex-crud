package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.jooq.Table;
import org.jooq.TableField;


public class JooqCollectionElement extends InternalFormElement<Table<?>, TableField<?,?>> {

    public JooqCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<Table<?>, TableField<?,?>> of(String label) {
        return new Builder<>(new JooqCollectionElement(label));
    }
}


