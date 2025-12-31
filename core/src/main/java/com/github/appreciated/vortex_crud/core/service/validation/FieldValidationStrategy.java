package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Validation strategy for field configurations.
 * Validates that ReferenceField configurations are complete and correct.
 */
public class FieldValidationStrategy implements ValidationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldValidationStrategy.class);

    @Override
    public void validate(Object value, String fieldName, String context) {
        // Check if the value is a ReferenceField
        if (value instanceof ReferenceField<?, ?, ?> refField) {
            validateReferenceField(refField, fieldName, context);
        }
        // Check if the value is a Map of fields
        else if ("fields".equals(fieldName) && value instanceof Map<?, ?>) {
            ((Map<?, ?>) value).forEach((key, field) -> {
                if (field instanceof ReferenceField<?, ?, ?> refField) {
                    validateReferenceField(refField, String.valueOf(key), context);
                }
            });
        }
        // Check if the value is a Collection of fields
        else if (value instanceof Collection<?>) {
            int index = 0;
            for (Object item : (Collection<?>) value) {
                if (item instanceof ReferenceField<?, ?, ?> refField) {
                    validateReferenceField(refField, fieldName + "[" + index + "]", context);
                }
                index++;
            }
        }
    }

    private void validateReferenceField(ReferenceField<?, ?, ?> refField, String fieldName, String context) {
        String fullContext = context + "." + fieldName;

        // Check if dataStore is configured
        if (refField.dataStore() == null) {
            LOGGER.error("ReferenceField '{}' has no dataStore configured. This will cause runtime errors. Context: {}",
                    fieldName, fullContext);
        }

        // Check if filterField is configured
        if (refField.filterField() == null) {
            LOGGER.warn("ReferenceField '{}' has no filterField configured. This may cause issues with searching. Context: {}",
                    fieldName, fullContext);
        }

        // Check if children fields are configured
        if (refField.children() == null || refField.children().isEmpty()) {
            LOGGER.warn("ReferenceField '{}' has no children fields configured. This may cause display issues. Context: {}",
                    fieldName, fullContext);
        }

        // Check if field is configured (the foreign key field itself)
        if (refField.field() == null) {
            LOGGER.warn("ReferenceField '{}' has no field configured. Context: {}",
                    fieldName, fullContext);
        }
    }
}
