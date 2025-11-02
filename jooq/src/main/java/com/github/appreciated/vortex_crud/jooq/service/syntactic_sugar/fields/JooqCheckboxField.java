package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.CheckboxField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqCheckboxField {
    public static 
    CheckboxField.CheckboxFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return CheckboxField.builder();
    }
}
