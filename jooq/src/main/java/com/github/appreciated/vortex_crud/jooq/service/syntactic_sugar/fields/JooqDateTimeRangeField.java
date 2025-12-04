package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateTimeRangeField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqDateTimeRangeField extends DateTimeRangeField<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {
    public static DateTimeRangeField.DateTimeRangeFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return DateTimeRangeField.builder();
    }
}
