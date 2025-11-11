package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqMultiSelectField {
    public static MultiSelectField.MultiSelectFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return MultiSelectField.builder();
    }
}
