package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class ConfigurationI18nValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationI18nValidator.class);
    private final TranslationService translationService;

    public ConfigurationI18nValidator(TranslationService translationService) {
        this.translationService = translationService;
    }

    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        LOGGER.info("Starting i18n configuration validation (Interface-based)...");
        List<Locale> locales = translationService.getProvidedLocales();
        if (locales.isEmpty()) {
            LOGGER.warn("No supported locales found. Skipping i18n validation.");
            return;
        }

        Collection<String> keys = application.getI18nKeys();
        for (String key : keys) {
            validateKey(key, locales);
        }

        LOGGER.info("I18n configuration validation completed. Checked {} unique keys across {} locales.", keys.size(), locales.size());
    }

    private void validateKey(String key, List<Locale> locales) {
        if (key == null || key.trim().isEmpty()) {
            return;
        }
        for (Locale locale : locales) {
            if (!translationService.containsKey(key, locale)) {
                LOGGER.warn("Missing i18n key '{}' for locale '{}'", key, locale);
            }
        }
    }
}
