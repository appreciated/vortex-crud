package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateTimePickerField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqDateTimePickerField {
    public static 
    DateTimePickerField.DateTimePickerFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return DateTimePickerField.builder();
    }
}
