package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.FormElement;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqFormElement {
    public static FormElement.FormElementBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>, ?, ?> of(TableField<?, ?> field, String label) {
        return FormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .field(field)
                .label(label);
    }
}

