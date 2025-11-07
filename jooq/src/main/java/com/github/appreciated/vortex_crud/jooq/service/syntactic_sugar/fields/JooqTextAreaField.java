package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqTextAreaField {
    public static TextAreaField.TextAreaFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return TextAreaField.builder();
    }
}
