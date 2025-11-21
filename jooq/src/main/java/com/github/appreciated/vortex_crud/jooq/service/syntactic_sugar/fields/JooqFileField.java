package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.FileField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqFileField {
    public static FileField.FileFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return FileField.builder();
    }
}
