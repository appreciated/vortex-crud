package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DoubleField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqDoubleField {
    public static DoubleField.DoubleFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return DoubleField.builder();
    }
}
