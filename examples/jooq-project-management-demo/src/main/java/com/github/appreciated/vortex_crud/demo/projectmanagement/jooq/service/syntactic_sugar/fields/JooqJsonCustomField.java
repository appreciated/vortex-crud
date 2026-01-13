package com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.demo.projectmanagement.ui.fields.JsonCustomField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

public class JooqJsonCustomField {
    public static JsonCustomField.JsonCustomFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return JsonCustomField.builder();
    }
}
