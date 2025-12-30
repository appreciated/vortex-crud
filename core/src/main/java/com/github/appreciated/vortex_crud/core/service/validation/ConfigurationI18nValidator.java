package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class ConfigurationI18nValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationI18nValidator.class);
    private final TranslationService translationService;

    public ConfigurationI18nValidator(TranslationService translationService) {
        this.translationService = translationService;
    }

    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        LOGGER.info("Starting i18n configuration validation (Reflection-based)...");
        List<Locale> locales = translationService.getProvidedLocales();
        if (locales.isEmpty()) {
            LOGGER.warn("No supported locales found. Skipping i18n validation.");
            return;
        }

        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        validateObject(application, visited, locales);

        LOGGER.info("I18n configuration validation completed.");
    }

    private void validateObject(Object target, Set<Object> visited, List<Locale> locales) {
        if (target == null || visited.contains(target)) {
            return;
        }
        visited.add(target);

        ReflectionUtils.doWithFields(target.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = field.get(target);

            if (value == null) {
                return;
            }

            if (field.isAnnotationPresent(I18nKey.class)) {
                validateValue(value, locales, field.getName(), target.getClass().getSimpleName());
            }

            // Recursion logic
            if (value instanceof Collection<?>) {
                for (Object item : (Collection<?>) value) {
                     validateObject(item, visited, locales);
                }
            } else if (value instanceof Map<?, ?>) {
                for (Object item : ((Map<?, ?>) value).values()) {
                    validateObject(item, visited, locales);
                }
            } else if (isComplexObject(value)) {
                validateObject(value, visited, locales);
            }
        });
    }

    private boolean isComplexObject(Object value) {
        String packageName = value.getClass().getPackage().getName();
        return packageName.startsWith("com.github.appreciated");
    }

    private void validateValue(Object value, List<Locale> locales, String fieldName, String className) {
        if (value instanceof String) {
            validateKey((String) value, locales, className + "." + fieldName);
        } else if (value instanceof Collection<?>) {
            ((Collection<?>) value).forEach(item -> {
                if (item instanceof String) {
                    validateKey((String) item, locales, className + "." + fieldName);
                }
            });
        } else if (value instanceof Map<?, ?>) {
             ((Map<?, ?>) value).values().forEach(item -> {
                 if (item instanceof String) {
                     validateKey((String) item, locales, className + "." + fieldName);
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
