package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.VideoField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqVideoField {
    public static 
    VideoField.VideoFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return VideoField.builder();
    }
}
