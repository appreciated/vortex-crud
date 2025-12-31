package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Validation strategy for i18n keys in configuration.
 * Validates that all i18n keys referenced in the configuration exist in the translation bundles.
 */
public class I18nValidationStrategy implements ValidationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nValidationStrategy.class);
    private final List<Locale> locales;
    private final TranslationService translationService;

    public I18nValidationStrategy(TranslationService translationService, List<Locale> locales) {
        this.translationService = translationService;
        this.locales = locales;
    }

    @Override
    public void validate(Object value, String fieldName, String context) {
        try {
            // Check if the field is annotated with @I18nKey
            Field field = findFieldInHierarchy(value.getClass().getDeclaringClass(), fieldName);

            if (field != null && field.isAnnotationPresent(I18nKey.class)) {
                validateI18nValue(value, locales, context + "." + fieldName);
            }
        } catch (Exception e) {
            // If we can't access the field metadata, skip validation for this field
        }
    }

    private Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }

        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    private void validateI18nValue(Object value, List<Locale> locales, String context) {
        if (value instanceof String) {
            validateKey((String) value, locales, context);
        } else if (value instanceof Collection<?>) {
            ((Collection<?>) value).forEach(item -> {
                if (item instanceof String) {
                    validateKey((String) item, locales, context);
                }
            });
        } else if (value instanceof Map<?, ?>) {
            ((Map<?, ?>) value).values().forEach(item -> {
                if (item instanceof String) {
                    validateKey((String) item, locales, context);
                }
            });
        }
    }

    private void validateKey(String key, List<Locale> locales, String context) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }
        for (Locale locale : locales) {
            if (!translationService.containsKey(key, locale)) {
                LOGGER.warn("Missing i18n key '{}' for locale '{}' in context: {}", key, locale, context);
            }
        }
    }
}
