package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.BigDecimalField;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;


public class JooqBigDecimalField {
    public static 
    BigDecimalField.BigDecimalFieldBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return BigDecimalField.builder();
    }
}
