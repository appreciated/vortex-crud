package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * Validates i18n key configuration at application startup.
 * Uses I18nValidationStrategy to check that all i18n keys exist in translation bundles.
 */
@Service
public class ConfigurationI18nValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationI18nValidator.class);
    private final TranslationService translationService;

    public ConfigurationI18nValidator(TranslationService translationService) {
        this.translationService = translationService;
    }

    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        List<Locale> locales = translationService.getProvidedLocales();
        if (locales.isEmpty()) {
            LOGGER.warn("No supported locales found. Skipping i18n validation.");
            return;
        }

        // Use the I18nValidationStrategy to validate i18n keys
        I18nValidationStrategy strategy = new I18nValidationStrategy(translationService, locales);
        application.validateWith(strategy);
    }
}
