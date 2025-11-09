package com.github.appreciated.vortex_crud.core.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class providing common validators for Vortex CRUD fields.
 * These validators can be directly used with Vaadin Flow Binder validation.
 *
 * <p>Example usage:</p>
 * <pre>
 * JooqTextField.builder()
 *     .validators(List.of(
 *         CommonValidators.stringMaxLength(255, "Maximum 255 characters allowed")
 *     ))
 *     .build()
 * </pre>
 */
public class CommonValidators {

    /**
     * Creates a validator that checks the maximum length of a string.
     *
     * @param maxLength the maximum allowed length
     * @param errorMessage the error message to display when validation fails
     * @return a validator for string length
     */
    public static Validator<String> stringMaxLength(int maxLength, String errorMessage) {
        return (value, context) -> {
            if (value == null || value.length() <= maxLength) {
                return ValidationResult.ok();
            }
            return ValidationResult.error(errorMessage);
        };
    }

    /**
     * Creates a validator that checks the maximum length of a string with a default error message.
     *
     * @param maxLength the maximum allowed length
     * @return a validator for string length
     */
    public static Validator<String> stringMaxLength(int maxLength) {
        return stringMaxLength(maxLength, "Maximum " + maxLength + " characters allowed");
    }

    /**
     * Creates a validator that checks the minimum length of a string.
     *
     * @param minLength the minimum required length
     * @param errorMessage the error message to display when validation fails
     * @return a validator for string length
     */
    public static Validator<String> stringMinLength(int minLength, String errorMessage) {
        return (value, context) -> {
            if (value == null || value.length() >= minLength) {
                return ValidationResult.ok();
            }
            return ValidationResult.error(errorMessage);
        };
    }

    /**
     * Creates a validator that checks the minimum length of a string with a default error message.
     *
     * @param minLength the minimum required length
     * @return a validator for string length
     */
    public static Validator<String> stringMinLength(int minLength) {
        return stringMinLength(minLength, "Minimum " + minLength + " characters required");
    }

    /**
     * Creates a validator that checks if a Double value is within a specified range.
     *
     * @param min the minimum allowed value (inclusive), or null for no minimum
     * @param max the maximum allowed value (inclusive), or null for no maximum
     * @param errorMessage the error message to display when validation fails
     * @return a validator for double range
     */
    public static Validator<Double> doubleRange(Double min, Double max, String errorMessage) {
        return (value, context) -> {
            if (value == null) {
                return ValidationResult.ok();
            }
            if (min != null && value < min) {
                return ValidationResult.error(errorMessage);
            }
            if (max != null && value > max) {
                return ValidationResult.error(errorMessage);
            }
            return ValidationResult.ok();
        };
    }

    /**
     * Creates a validator that checks if a Double value is within a specified range with a default error message.
     *
     * @param min the minimum allowed value (inclusive), or null for no minimum
     * @param max the maximum allowed value (inclusive), or null for no maximum
     * @return a validator for double range
     */
    public static Validator<Double> doubleRange(Double min, Double max) {
        String message = "Value must be";
        if (min != null && max != null) {
            message += " between " + min + " and " + max;
        } else if (min != null) {
            message += " at least " + min;
        } else if (max != null) {
            message += " at most " + max;
        }
        return doubleRange(min, max, message);
    }

    /**
     * Creates a validator that checks if a Double value is at least the specified minimum.
     *
     * @param min the minimum allowed value (inclusive)
     * @param errorMessage the error message to display when validation fails
     * @return a validator for double minimum
     */
    public static Validator<Double> doubleMin(Double min, String errorMessage) {
        return doubleRange(min, null, errorMessage);
    }

    /**
     * Creates a validator that checks if a Double value is at least the specified minimum with a default error message.
     *
     * @param min the minimum allowed value (inclusive)
     * @return a validator for double minimum
     */
    public static Validator<Double> doubleMin(Double min) {
        return doubleRange(min, null);
    }

    /**
     * Creates a validator that checks if a Double value is at most the specified maximum.
     *
     * @param max the maximum allowed value (inclusive)
     * @param errorMessage the error message to display when validation fails
     * @return a validator for double maximum
     */
    public static Validator<Double> doubleMax(Double max, String errorMessage) {
        return doubleRange(null, max, errorMessage);
    }

    /**
     * Creates a validator that checks if a Double value is at most the specified maximum with a default error message.
     *
     * @param max the maximum allowed value (inclusive)
     * @return a validator for double maximum
     */
    public static Validator<Double> doubleMax(Double max) {
        return doubleRange(null, max);
    }

    /**
     * Creates a validator that checks if an Integer value is within a specified range.
     *
     * @param min the minimum allowed value (inclusive), or null for no minimum
     * @param max the maximum allowed value (inclusive), or null for no maximum
     * @param errorMessage the error message to display when validation fails
     * @return a validator for integer range
     */
    public static Validator<Integer> integerRange(Integer min, Integer max, String errorMessage) {
        return (value, context) -> {
            if (value == null) {
                return ValidationResult.ok();
            }
            if (min != null && value < min) {
                return ValidationResult.error(errorMessage);
            }
            if (max != null && value > max) {
                return ValidationResult.error(errorMessage);
            }
            return ValidationResult.ok();
        };
    }

    /**
     * Creates a validator that checks if an Integer value is within a specified range with a default error message.
     *
     * @param min the minimum allowed value (inclusive), or null for no minimum
     * @param max the maximum allowed value (inclusive), or null for no maximum
     * @return a validator for integer range
     */
    public static Validator<Integer> integerRange(Integer min, Integer max) {
        String message = "Value must be";
        if (min != null && max != null) {
            message += " between " + min + " and " + max;
        } else if (min != null) {
            message += " at least " + min;
        } else if (max != null) {
            message += " at most " + max;
        }
        return integerRange(min, max, message);
    }

    /**
     * Creates a validator that checks if a LocalDate is within a specified range.
     *
     * @param min the minimum allowed date (inclusive), or null for no minimum
     * @param max the maximum allowed date (inclusive), or null for no maximum
     * @param errorMessage the error message to display when validation fails
     * @return a validator for date range
     */
    public static Validator<LocalDate> dateRange(LocalDate min, LocalDate max, String errorMessage) {
        return (value, context) -> {
            if (value == null) {
                return ValidationResult.ok();
            }
            if (min != null && value.isBefore(min)) {
                return ValidationResult.error(errorMessage);
            }
            if (max != null && value.isAfter(max)) {
                return ValidationResult.error(errorMessage);
            }
            return ValidationResult.ok();
        };
    }

    /**
     * Creates a validator that checks if a LocalDate is within a specified range with a default error message.
     *
     * @param min the minimum allowed date (inclusive), or null for no minimum
     * @param max the maximum allowed date (inclusive), or null for no maximum
     * @return a validator for date range
     */
    public static Validator<LocalDate> dateRange(LocalDate min, LocalDate max) {
        String message = "Date must be";
        if (min != null && max != null) {
            message += " between " + min + " and " + max;
        } else if (min != null) {
            message += " after or on " + min;
        } else if (max != null) {
            message += " before or on " + max;
        }
        return dateRange(min, max, message);
    }

    /**
     * Creates a validator that checks if a string matches a regular expression pattern.
     *
     * @param pattern the regular expression pattern to match
     * @param errorMessage the error message to display when validation fails
     * @return a validator for pattern matching
     */
    public static Validator<String> pattern(String pattern, String errorMessage) {
        return (value, context) -> {
            if (value == null || value.matches(pattern)) {
                return ValidationResult.ok();
            }
            return ValidationResult.error(errorMessage);
        };
    }

    /**
     * Creates a validator that checks if a string is not empty.
     *
     * @param errorMessage the error message to display when validation fails
     * @return a validator for non-empty strings
     */
    public static Validator<String> notEmpty(String errorMessage) {
        return (value, context) -> {
            if (value != null && !value.trim().isEmpty()) {
                return ValidationResult.ok();
            }
            return ValidationResult.error(errorMessage);
        };
    }

    /**
     * Creates a validator that checks if a string is not empty with a default error message.
     *
     * @return a validator for non-empty strings
     */
    public static Validator<String> notEmpty() {
        return notEmpty("Value cannot be empty");
    }
}
