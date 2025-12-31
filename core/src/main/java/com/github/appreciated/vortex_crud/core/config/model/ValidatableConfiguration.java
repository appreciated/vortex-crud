package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.service.validation.ValidationStrategy;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Interface for configuration objects that support validation.
 * Provides recursive traversal of the configuration tree with cycle detection.
 */
public interface ValidatableConfiguration {

    /**
     * Validates all fields in the configuration tree using the provided strategy.
     *
     * @param strategy The validation strategy to apply to each field
     */
    default void validateWith(ValidationStrategy strategy) {
        validateWith(strategy, Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    /**
     * Internal method for recursive validation with cycle detection.
     *
     * @param strategy The validation strategy to apply
     * @param visited Set of already visited objects to prevent infinite recursion
     */
    default void validateWith(ValidationStrategy strategy, Set<Object> visited) {
        if (visited.contains(this)) {
            return;
        }
        visited.add(this);

        String context = this.getClass().getSimpleName();

        ReflectionUtils.doWithFields(this.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(this);

            if (value == null) {
                return;
            }

            String fieldName = field.getName();

            // Apply validation strategy to this field
            strategy.validate(value, fieldName, context);

            // Recurse into child objects
            validateChild(value, strategy, visited);
        });
    }

    private void validateChild(Object value, ValidationStrategy strategy, Set<Object> visited) {
        if (value instanceof ValidatableConfiguration) {
            ((ValidatableConfiguration) value).validateWith(strategy, visited);
        } else if (value instanceof java.util.Collection<?>) {
            for (Object item : (java.util.Collection<?>) value) {
                if (item instanceof ValidatableConfiguration) {
                    ((ValidatableConfiguration) item).validateWith(strategy, visited);
                }
            }
        } else if (value instanceof Map<?, ?>) {
            for (Object item : ((Map<?, ?>) value).values()) {
                if (item instanceof ValidatableConfiguration) {
                    ((ValidatableConfiguration) item).validateWith(strategy, visited);
                }
            }
        }
    }
}
