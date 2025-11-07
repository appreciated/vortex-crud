package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.EmailField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqEmailField {
    public static EmailField.EmailFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return EmailField.builder();
    }
}
