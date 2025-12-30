package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ConfigurationI18nValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationI18nValidator.class);
    private final TranslationService translationService;

    public ConfigurationI18nValidator(TranslationService translationService) {
        this.translationService = translationService;
    }

    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        LOGGER.info("Starting i18n configuration validation (Visitor-based)...");

        if (translationService.getProvidedLocales().isEmpty()) {
            LOGGER.warn("No supported locales found. Skipping i18n validation.");
            return;
        }

        I18nValidationVisitor visitor = new I18nValidationVisitor(translationService);
        application.accept(visitor);

        LOGGER.info("I18n configuration validation completed.");
    }
}
