package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.jooq.impl.TableImpl;
import org.jooq.TableField;
import org.jooq.TableRecord;


public class JooqCollectionElement extends InternalFormElement<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public JooqCollectionElement(String label) {
        super(null, "collection", label);
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(String label) {
        return new Builder<>(new JooqCollectionElement(label));
    }
}


