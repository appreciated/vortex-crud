package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;
import org.jooq.Table;
import org.jooq.TableField;

public class JooqFormElement extends InternalFormElement<Table<?>, TableField<?,?>> {

    public JooqFormElement(TableField<?, ?> field, String type, String label) {
        super(field, type, label);
    }

    public static Builder<Table<?>, TableField<?,?>> of(TableField<?,?> field, String type, String label) {
        return new Builder<>(new JooqFormElement(field, type, label));
    }
}

