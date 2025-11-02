package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.PasswordField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqPasswordField {
    public static 
    PasswordField.PasswordFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return PasswordField.builder();
    }
}
