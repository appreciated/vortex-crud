package com.github.appreciated.vortex_crud.security.core.view;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignUpViewTest {

    @Mock
    private VortexCrudConfigService<String, String, String> configService;
    @Mock
    private FormCreator<String, String, String> formCreator;
    @Mock
    private ReflectionService<String> reflectionService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Application<String, String, String> application;
    @Mock
    private IdentityAndAccessManagement<String, String, String> identityAndAccessManagement;
    @Mock
    private VortexCrudDataStore dataStore;
    @Mock
    private Map<String, DataStoreConfig<String, String, String>> dataStoresMap;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(application.dataStores()).thenReturn(dataStoresMap);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    void testInstantiation() {
        when(identityAndAccessManagement.repositoryKey()).thenReturn("userRepo");

        DataStoreConfig<String, String, String> dsConfig = mock(DataStoreConfig.class);
        when(dataStoresMap.get("userRepo")).thenReturn(dsConfig);
        when(dsConfig.dataStoreInstance()).thenReturn(dataStore);

        when(dataStore.newInstance()).thenReturn(new Object());

        SignUpView<String, String, String> view = new SignUpView<>(
                configService,
                formCreator,
                reflectionService,
                passwordEncoder
        );

        assertNotNull(view);
    }
}
