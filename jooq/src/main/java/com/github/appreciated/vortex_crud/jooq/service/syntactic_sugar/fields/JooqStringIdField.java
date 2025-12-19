package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.StringIdField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqStringIdField {
    public static StringIdField.StringIdFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return StringIdField.builder();
    }
}
