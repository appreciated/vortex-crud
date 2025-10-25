package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFieldElement extends InternalFormElement<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public JooqFieldElement(TableField<?, ?> field, String label) {
        super(field, ViewFieldType.FIELD, label);
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(TableField<?, ?> field, String label) {
        return new Builder<>(new JooqFieldElement(field, label));
    }
}

