package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFormElement {
    public static InternalFormElement.InternalFormElementBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>, ?, ?> of(TableField<?, ?> field, String label) {
        return InternalFormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .field(field)
                .label(label)
                .type(ViewFieldType.FIELD);
    }
}

