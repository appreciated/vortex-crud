package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.visitor.ConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class I18nValidationVisitor implements ConfigurationVisitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18nValidationVisitor.class);
    private final TranslationService translationService;
    private final List<Locale> locales;
    private final Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<>());

    public I18nValidationVisitor(TranslationService translationService) {
        this.translationService = translationService;
        this.locales = translationService.getProvidedLocales();
    }

    @Override
    public void visit(Application<?, ?, ?> application) {
        validateFields(application);
    }

    @Override
    public void visit(RouteRenderer<?, ?, ?> routeRenderer) {
        validateFields(routeRenderer);
    }

    @Override
    public void visit(DataStoreConfig<?, ?, ?> dataStoreConfig) {
        validateFields(dataStoreConfig);
    }

    @Override
    public void visit(com.github.appreciated.vortex_crud.core.config.model.Field<?, ?, ?> field) {
        validateFields(field);
    }

    @Override
    public void visit(InternalFormElement<?, ?, ?> internalFormElement) {
        validateFields(internalFormElement);
    }

    @Override
    public void visit(Object object) {
        validateFields(object);
    }

    private void validateFields(Object target) {
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
                validateValue(value, field.getName(), target.getClass().getSimpleName());
            } else if (value instanceof Map<?, ?> || value instanceof java.util.Collection<?>) {
                 // Already discussed.
            }
        });
    }

    private void validateValue(Object value, String fieldName, String className) {
        if (value instanceof String) {
            validateKey((String) value, className + "." + fieldName);
        } else if (value instanceof java.util.Collection<?>) {
            ((java.util.Collection<?>) value).forEach(item -> {
                if (item instanceof String) {
                    validateKey((String) item, className + "." + fieldName);
                }
            });
        } else if (value instanceof Map<?, ?>) {
            ((Map<?, ?>) value).values().forEach(item -> {
                if (item instanceof String) {
                    validateKey((String) item, className + "." + fieldName);
                }
            });
        }
    }

    private void validateKey(String key, String context) {
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
