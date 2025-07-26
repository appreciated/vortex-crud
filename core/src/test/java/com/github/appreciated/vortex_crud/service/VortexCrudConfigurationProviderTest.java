package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VortexCrudConfigurationProviderTest {

    @Mock
    private Application<String, String, String> application;

    private TestVortexCrudConfigurationProvider configProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configProvider = new TestVortexCrudConfigurationProvider(application);
    }

    @Test
    void testGet() {
        Application<String, String, String> result = configProvider.get();
        assertNotNull(result, "Configuration should not be null");
        assertEquals(application, result, "Configuration should match the mocked application");
    }

    /**
     * Test implementation of VortexCrudConfigurationProvider for testing purposes.
     */
    private static class TestVortexCrudConfigurationProvider implements VortexCrudConfigurationProvider<String, String, String> {

        private final Application<String, String, String> application;

        public TestVortexCrudConfigurationProvider(Application<String, String, String> application) {
            this.application = application;
        }

        @Override
        public Application<String, String, String> get() {
            return application;
        }
    }
}