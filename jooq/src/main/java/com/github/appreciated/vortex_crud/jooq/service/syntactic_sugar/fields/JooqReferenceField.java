package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqReferenceField {
    public static ReferenceField.ReferenceFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return ReferenceField.builder();
    }
}
