package com.github.appreciated.vortex_crud.core.service.validation;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class ConfigurationI18nValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationI18nValidator.class);
    private final TranslationService translationService;

    public ConfigurationI18nValidator(TranslationService translationService) {
        this.translationService = translationService;
    }

    public <ModelClass, FieldType, RepositoryType> void validate(Application<ModelClass, FieldType, RepositoryType> application) {
        LOGGER.info("Starting i18n configuration validation...");
        List<Locale> locales = translationService.getProvidedLocales();
        if (locales.isEmpty()) {
            LOGGER.warn("No supported locales found. Skipping i18n validation.");
            return;
        }

        validateKey(application.applicationName(), locales, "Application Name");

        if (application.selects() != null && application.selects().configs() != null) {
            application.selects().configs().forEach((selectKey, options) -> {
                if (options != null) {
                    options.values().forEach(value -> validateKey(value, locales, "Select Option (" + selectKey + ")"));
                }
            });
        }

        if (application.notificationPanelConfiguration() != null) {
            validateNotificationConfiguration(application.notificationPanelConfiguration(), locales);
        }

        if (application.defaultMenuActions() != null) {
            application.defaultMenuActions().forEach(action -> validateMenuAction(action, locales));
        }

        if (application.menuActions() != null) {
            application.menuActions().forEach(action -> validateMenuAction(action, locales));
        }

        if (application.routes() != null) {
            application.routes().forEach((path, route) -> validateRoute(route, locales, path));
        }

        LOGGER.info("I18n configuration validation completed.");
    }

    private <ModelClass, FieldType, RepositoryType> void validateRoute(RouteRenderer<ModelClass, FieldType, RepositoryType> route, List<Locale> locales, String path) {
        validateKey(route.title(), locales, "Route Title (" + path + ")");

        if (route.menuActions() != null) {
            route.menuActions().forEach(action -> validateMenuAction(action, locales));
        }

        if (route instanceof RouteRendererMultipleChildren) {
            Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> children =
                    ((RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType>) route).childrenMap();
            if (children != null) {
                children.forEach((childPath, childRoute) -> validateRoute(childRoute, locales, path + "/" + childPath));
            }
        }

        if (route instanceof RouteRendererSingleChild) {
            RouteRenderer<ModelClass, FieldType, RepositoryType> childRoute =
                    ((RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) route).form();
            if (childRoute != null) {
                validateRoute(childRoute, locales, path + "/child");
            }
        }

        if (route.children() != null) {
            for (InternalFormElement<ModelClass, FieldType, RepositoryType> child : route.children()) {
                validateInternalFormElement(child, locales, path);
            }
        }
    }

    private <ModelClass, FieldType, RepositoryType> void validateInternalFormElement(InternalFormElement<ModelClass, FieldType, RepositoryType> element, List<Locale> locales, String path) {
        validateKey(element.label(), locales, "Form Element Label (" + path + ")");
        if (element.configuration() != null) {
            validateCollectionConfiguration(element.configuration(), locales, path);
        }
    }

    private <ModelClass, FieldType, RepositoryType> void validateCollectionConfiguration(com.github.appreciated.vortex_crud.core.config.model.Collection<ModelClass, FieldType, RepositoryType> collection, List<Locale> locales, String path) {
        validateKey(collection.label(), locales, "Collection Label (" + path + ")");
        validateKey(collection.emptyMessage(), locales, "Collection Empty Message (" + path + ")");
        if (collection.form() != null) {
            validateRoute(collection.form(), locales, path + "/collection-form");
        }
    }

    private <ModelClass, FieldType, RepositoryType> void validateMenuAction(DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType> action, List<Locale> locales) {
        validateKey(action.label(), locales, "Menu Action Label");
        validateKey(action.placeholder(), locales, "Menu Action Placeholder");
    }

    private <ModelClass, FieldType, RepositoryType> void validateNotificationConfiguration(NotificationPanelConfiguration<ModelClass, FieldType, RepositoryType> config, List<Locale> locales) {
        validateKey(config.headingKey(), locales, "Notification Heading");
        validateKey(config.unreadTabKey(), locales, "Notification Unread Tab");
        validateKey(config.allTabKey(), locales, "Notification All Tab");
        validateKey(config.markAllReadKey(), locales, "Notification Mark All Read");
        validateKey(config.noNewNotificationsKey(), locales, "Notification No New");
        validateKey(config.ariaLabel(), locales, "Notification Aria Label");
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
