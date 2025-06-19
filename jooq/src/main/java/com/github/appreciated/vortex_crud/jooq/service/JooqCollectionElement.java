package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.jooq.TableField;
import org.jooq.TableRecord;


public class JooqCollectionElement extends InternalFormElement<Class<? extends TableRecord<?>>, TableField<?,?>> {

    public JooqCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<Class<? extends TableRecord<?>>, TableField<?,?>> of(String label) {
        return new Builder<>(new JooqCollectionElement(label));
    }
}


