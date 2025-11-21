package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectValueField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqMultiSelectValueField {
    public static MultiSelectValueField.MultiSelectValueFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return MultiSelectValueField.builder();
    }
}
