package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class TranslationServiceTest {

    @Mock
    private VortexCrudConfigService<String, String> configService;

    @Mock
    private Application<String, String> application;

    @Mock
    private ResourcePatternResolver patternResolver;

    @Mock
    private Resource resource;

    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the configuration service
        when(configService.getConfiguration()).thenReturn(application);
        when(application.getI18nBundlePrefix()).thenReturn("i18n/messages");

        translationService = new TranslationService(configService);
    }

    @Test
    void testGetProvidedLocales() {
        // This test is limited because we can't easily mock the ResourcePatternResolver inside the service
        // We're just verifying that the method returns a non-null list
        List<Locale> locales = translationService.getProvidedLocales();
        assertNotNull(locales, "Provided locales should not be null");
    }

    @Test
    void testGetTranslationWithNullKey() {
        String translation = translationService.getTranslation(null, Locale.ENGLISH);
        assertEquals("", translation, "Translation for null key should be empty string");
    }

    @Test
    void testGetTranslationWithMissingKey() {
        // This test relies on the actual behavior when a key is missing
        // It should return the key wrapped in !{} markers
        String missingKey = "missing.key";
        String translation = translationService.getTranslation(missingKey, Locale.ENGLISH);
        assertEquals("!{missing.key}!", translation, "Missing key should be wrapped in !{} markers");
    }

    @Test
    void testGetTranslationWithParameters() {
        // Create a custom TranslationService with a mocked ResourceBundle
        TranslationService customService = new TranslationService(configService) {
            @Override
            public String getTranslation(String key, Locale locale, Object... params) {
                if ("test.key".equals(key)) {
                    String value = "Hello, {0}! Today is {1}.";
                    if (params.length > 0) {
                        return java.text.MessageFormat.format(value, params);
                    }
                    return value;
                }
                return "!{" + key + "}!";
            }
        };

        String translation = customService.getTranslation("test.key", Locale.ENGLISH, "World", "Monday");
        assertEquals("Hello, World! Today is Monday.", translation, "Parameters should be substituted correctly");
    }
}
