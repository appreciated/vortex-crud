package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.MarkDownField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqMarkDownField {
    public static MarkDownField.MarkDownFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return MarkDownField.builder();
    }
}
