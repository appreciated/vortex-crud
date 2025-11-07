package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqSelectField {
    public static SelectField.SelectFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return SelectField.builder();
    }
}
