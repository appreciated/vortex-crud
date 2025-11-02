package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.IntegerField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqIntegerField {
    public static 
    IntegerField.IntegerFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return IntegerField.builder();
    }
}
