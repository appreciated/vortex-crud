package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqTextField {
    public static 
    TextField.TextFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return TextField.builder();
    }
}
