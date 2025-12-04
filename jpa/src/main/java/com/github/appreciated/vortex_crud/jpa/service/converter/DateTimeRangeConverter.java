package com.github.appreciated.vortex_crud.jpa.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.appreciated.vortex_crud.core.entity.DateTimeRange;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DateTimeRangeConverter implements AttributeConverter<DateTimeRange, String> {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String convertToDatabaseColumn(DateTimeRange attribute) {
        try {
            return attribute == null ? null : mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DateTimeRange convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : mapper.readValue(dbData, DateTimeRange.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
