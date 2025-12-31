package com.github.appreciated.vortex_crud.core.service.validation;

/**
 * Strategy interface for validating configuration objects during traversal.
 * Implementations can define specific validation logic for different aspects
 * of the configuration (e.g., i18n keys, field configurations, etc.).
 */
@FunctionalInterface
public interface ValidationStrategy {
    /**
     * Validates a value encountered during configuration traversal.
     *
     * @param value The value to validate
     * @param fieldName The name of the field containing the value
     * @param context The context where the value was found (e.g., class name)
     */
    void validate(Object value, String fieldName, String context);
}
