package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateRangeField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqDateRangeField extends DateRangeField<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static DateRangeField.DateRangeFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return DateRangeField.builder();
    }
}
