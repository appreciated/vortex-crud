package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqImageField {
    public static 
    ImageField.ImageFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return ImageField.builder();
    }
}
