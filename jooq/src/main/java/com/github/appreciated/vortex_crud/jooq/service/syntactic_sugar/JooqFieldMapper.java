package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class JooqFieldMapper {

    @SuppressWarnings("unchecked")
    public static Field<TableRecord<?>, TableField<?, ?>, TableImpl<?>> map(TableField<?, ?> field) {
        Class<?> type = field.getType();

        if (Integer.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return JooqIntegerField.builder().build();
        } else if (String.class.isAssignableFrom(type)) {
             // Heuristic: Long text -> TextArea
             if (field.getDataType().length() > 255) {
                 return JooqTextAreaField.builder().build();
             }
             return JooqTextField.builder().build();
        } else if (Boolean.class.isAssignableFrom(type)) {
            return JooqCheckboxField.builder().build();
        } else if (LocalDate.class.isAssignableFrom(type)) {
            return JooqDateField.builder().build();
        } else if (LocalDateTime.class.isAssignableFrom(type) || java.sql.Timestamp.class.isAssignableFrom(type) || OffsetDateTime.class.isAssignableFrom(type)) {
            return JooqDateTimePickerField.builder().build();
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            return JooqBigDecimalField.builder().build();
        } else if (Double.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return JooqDoubleField.builder().build();
        }

        return null;
    }
}
