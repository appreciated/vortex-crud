package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields;

import com.github.appreciated.vortex_crud.core.config.model.fields.DateRangeField;
import com.vaadin.flow.data.binder.Validator;
import lombok.Builder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

public class JooqDateRangeField {
    @Builder
    public static DateRangeField<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder(List<Validator<?>> validators, boolean required) {
        return DateRangeField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .validators(validators)
                .required(required)
                .build();
    }
}
