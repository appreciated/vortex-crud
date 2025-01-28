package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.jooq.Table;
import org.jooq.TableField;


public class JooqFieldElement extends InternalFormElement<Table<?>, TableField<?,?>> {

    public JooqFieldElement(TableField<?, ?> field, String label) {
        super(field, "field", label);
    }

    public static Builder<Table<?>, TableField<?,?>> of(TableField<?,?> field, String label) {
        return new Builder<>(new JooqFieldElement(field, label));
    }
}


