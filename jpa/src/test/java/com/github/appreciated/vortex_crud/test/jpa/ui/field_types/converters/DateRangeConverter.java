package com.github.appreciated.vortex_crud.test.jpa.ui.field_types.converters;

import com.github.appreciated.vortex_crud.core.entity.DateRange;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DateRangeConverter implements AttributeConverter<DateRange, String> {

    @Override
    public String convertToDatabaseColumn(DateRange attribute) {
        if (attribute == null) {
            return null;
        }
        return (attribute.getStart() != null ? attribute.getStart() : "") +
               "," +
               (attribute.getEnd() != null ? attribute.getEnd() : "");
    }

    @Override
    public DateRange convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(",", -1);
        java.time.LocalDate start = parts.length > 0 && !parts[0].isEmpty() ? java.time.LocalDate.parse(parts[0]) : null;
        java.time.LocalDate end = parts.length > 1 && !parts[1].isEmpty() ? java.time.LocalDate.parse(parts[1]) : null;
        return new DateRange(start, end);
    }
}
