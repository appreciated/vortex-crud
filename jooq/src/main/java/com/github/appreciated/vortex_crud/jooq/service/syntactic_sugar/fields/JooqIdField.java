package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqIdField {
    public static IdField.IdFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return IdField.builder();
    }
}
