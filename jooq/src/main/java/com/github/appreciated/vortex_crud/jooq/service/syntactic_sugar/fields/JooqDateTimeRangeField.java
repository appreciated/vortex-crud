package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateTimeRangeField;
import com.vaadin.flow.data.binder.Validator;
import lombok.Builder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

public class JooqDateTimeRangeField {
    @Builder
    public static DateTimeRangeField<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder(List<Validator<?>> validators, boolean required) {
        return DateTimeRangeField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .validators(validators)
                .required(required)
                .build();
    }
}
