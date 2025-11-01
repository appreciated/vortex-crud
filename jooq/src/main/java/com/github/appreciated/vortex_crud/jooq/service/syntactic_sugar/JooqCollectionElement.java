package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.config.model.ViewFieldType;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqCollectionElement {
    public static InternalFormElement.InternalFormElementBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(String label) {
        return InternalFormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .label(label)
                .type(ViewFieldType.COLLECTION);
    }
}

