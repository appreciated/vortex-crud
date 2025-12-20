package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqNumericIdField {
    public static NumericIdField.NumericIdFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return NumericIdField.builder();
    }
}
