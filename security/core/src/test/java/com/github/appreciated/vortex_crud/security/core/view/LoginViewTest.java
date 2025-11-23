package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class LoginViewTest {

    @Mock
    private VortexCrudConfigService<String, String, String> configService;
    @Mock
    private VortexCrudDataStoreFactoryRegistry<String, String, String> dataStoreFactoryRegistry;
    @Mock
    private ReflectionService<String> reflectionService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Application<String, String, String> application;
    @Mock
    private IdentityAndAccessManagement<String, String, String> identityAndAccessManagement;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(application.applicationName()).thenReturn("Test App");
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void testInstantiation() {
        LoginView<String, String, String> view = new LoginView<>(
                configService,
                dataStoreFactoryRegistry,
                reflectionService,
                passwordEncoder
        );

        assertNotNull(view);
    }
}
