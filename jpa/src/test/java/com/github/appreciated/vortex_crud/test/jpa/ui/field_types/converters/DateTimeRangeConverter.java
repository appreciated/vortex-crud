package com.github.appreciated.vortex_crud.test.jpa.ui.field_types.converters;

import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DateTimeRangeConverter implements AttributeConverter<DateTimeRange, String> {

    @Override
    public String convertToDatabaseColumn(DateTimeRange attribute) {
        if (attribute == null) {
            return null;
        }
        return (attribute.getStart() != null ? attribute.getStart() : "") +
               "," +
               (attribute.getEnd() != null ? attribute.getEnd() : "");
    }

    @Override
    public DateTimeRange convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(",", -1);
        java.time.LocalDateTime start = parts.length > 0 && !parts[0].isEmpty() ? java.time.LocalDateTime.parse(parts[0]) : null;
        java.time.LocalDateTime end = parts.length > 1 && !parts[1].isEmpty() ? java.time.LocalDateTime.parse(parts[1]) : null;
        return new DateTimeRange(start, end);
    }
}
