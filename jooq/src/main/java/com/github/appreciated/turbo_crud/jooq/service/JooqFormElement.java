package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.InternalFormElement;
import org.jooq.Table;

public class JooqFormElement extends InternalFormElement<Table<?>> {
    public JooqFormElement() {
    }

    public JooqFormElement(String field, String type, String label) {
        super(field, type, label);
    }

    public static Builder<Table<?>> of(String field, String type, String label) {
        return new Builder<>(new InternalFormElement<>(field, type, label));
    }
}

