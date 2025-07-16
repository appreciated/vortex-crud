package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import org.jooq.TableField;
import org.jooq.TableRecord;


public class JooqFieldElement extends InternalFormElement<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> {

    public JooqFieldElement(TableField<?, ?> field, String label) {
        super(field, "field", label);
    }

    public static Builder<TableRecord<?>, TableField<?, ?>, Class<? extends TableRecord<?>>> of(TableField<?,?> field, String label) {
        return new Builder<>(new JooqFieldElement(field, label));
    }
}


