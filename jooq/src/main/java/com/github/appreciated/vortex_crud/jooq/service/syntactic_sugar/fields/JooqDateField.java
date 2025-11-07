package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqDateField {
    public static DateField.DateFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return DateField.builder();
    }
}
