package com.github.appreciated.flow_cms.service;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class TranslationService implements I18NProvider {

    public final Locale LOCALE_DE = new Locale("de", "DE");
    public final Locale LOCALE_EN = new Locale("en", "EN");

    private final List<Locale> locales = List.of(LOCALE_DE, LOCALE_EN);
    private final String i18nBundlePrefix;

    public TranslationService(FlowCmsConfigService flowCmsConfigService) {
        i18nBundlePrefix = flowCmsConfigService.getConfiguration().getI18nBundlePrefix();
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Got lang request for key with null value!");
            return "";
        }


        final ResourceBundle bundle = ResourceBundle.getBundle(i18nBundlePrefix, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(TranslationService.class.getName())
                    .warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}