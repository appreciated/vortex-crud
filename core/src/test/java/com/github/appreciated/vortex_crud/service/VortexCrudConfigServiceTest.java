package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class VortexCrudConfigServiceTest {

    @Mock
    private VortexCrudConfigurationProvider<String, String, String> configProvider;

    @Mock
    private Application<String, String, String> application;

    private TestVortexCrudConfigService configService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the configuration provider
        when(configProvider.get()).thenReturn(application);
        when(application.applicationName()).thenReturn("Test Application");

        configService = new TestVortexCrudConfigService(configProvider);
    }

    @Test
    void testconfiguration() {
        Application<String, String, String> config = configService.configuration();
        assertNotNull(config, "Configuration should not be null");
        assertEquals(application, config, "Configuration should match the mocked application");
    }

    @Test
    void testapplicationName() {
        String appName = configService.applicationName();
        assertEquals("Test Application", appName, "Application name should match the mocked name");
    }

    /**
     * Test implementation of VortexCrudConfigService for testing purposes.
     */
    private static class TestVortexCrudConfigService implements VortexCrudConfigService<String, String, String> {

        private final VortexCrudConfigurationProvider<String, String, String> configProvider;

        public TestVortexCrudConfigService(VortexCrudConfigurationProvider<String, String, String> configProvider) {
            this.configProvider = configProvider;
        }

        @Override
        public Application<String, String, String> configuration() {
            return configProvider.get();
        }

        @Override
        public String applicationName() {
            return configuration().applicationName();
        }
    }
}