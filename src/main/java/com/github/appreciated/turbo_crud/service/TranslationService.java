package com.github.appreciated.turbo_crud.service;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TranslationService provides internationalization (i18n) services for the application.
 * This service implements the {@link I18NProvider} interface to support translation
 * functionalities using resource bundles for various locales.
 */
@Service
public class TranslationService implements I18NProvider {

    private final List<Locale> supportedLocales;
    private final String i18nBundlePrefix;

    /**
     * Constructor for the TranslationService.
     *
     * @param configService The configuration service to get the i18n bundle prefix.
     */
    public TranslationService(TurboCrudConfigService configService) {
        this.i18nBundlePrefix = configService.getConfiguration().getI18nBundlePrefix();
        this.supportedLocales = discoverAvailableLocales();
    }

    /**
     * Discovers available locales by checking for available resource bundles in the classpath.
     *
     * @return A list of available {@link Locale} objects.
     */
    private List<Locale> discoverAvailableLocales() {
        List<Locale> availableLocales = new ArrayList<>();
        try {
            ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] mappingLocations = patternResolver.getResources("classpath:" + i18nBundlePrefix + "*.properties");

            for (Resource mappingLocation : mappingLocations) {
                availableLocales.addAll(parseAvailableLocales(mappingLocation));
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Error while discovering resource bundles: ", e);
        }

        availableLocales.sort((locale1, locale2) -> {
            if (locale1.getLanguage().equals("en") && !locale2.getLanguage().equals("en")) {
                return -1;
            } else if (!locale1.getLanguage().equals("en") && locale2.getLanguage().equals("en")) {
                return 1;
            } else {
                return locale1.toString().compareTo(locale2.toString());
            }
        });

        return availableLocales;
    }

    /**
     * Parses available locales from resource bundle files in the given URL path.
     *
     * @param resource The resource bundle directory.
     * @return A list of parsed {@link Locale} objects.
     */
    private List<Locale> parseAvailableLocales(Resource resource) {
        List<Locale> detectedLocales = new ArrayList<>();

        Pattern pattern = Pattern.compile(i18nBundlePrefix + "_([a-z]{2})(_[A-Z]{2})?\\.properties");

        String filename = resource.getFilename();
        if (filename != null) {
            Matcher matcher = pattern.matcher(filename);
            if (matcher.matches()) {
                String language = matcher.group(1);
                String country = matcher.group(2) != null ? matcher.group(2).substring(1) : "";
                Locale locale = country.isEmpty() ? new Locale(language) : new Locale(language, country);
                detectedLocales.add(locale);
            }
        } else {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Resource filename is null for resource: " + resource);
        }

        return detectedLocales;
    }

    /**
     * Returns the list of provided locales supported by this service.
     *
     * @return A list of {@link Locale} objects representing the supported locales.
     */
    @Override
    public List<Locale> getProvidedLocales() {
        return supportedLocales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Got lang request for key with null value!");
            return "";
        }

        String value;
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(i18nBundlePrefix, locale);
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Missing resource", e);
            return "!{%s}!".formatted(key);
        }

        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }

}
