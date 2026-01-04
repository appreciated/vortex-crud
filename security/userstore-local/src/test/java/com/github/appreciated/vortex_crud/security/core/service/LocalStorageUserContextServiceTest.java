package com.github.appreciated.vortex_crud.security.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.InternalFormElement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Simple tests for LocalStorageUserContextService.
 * These tests use mocking to verify the important functionality of the service.
 */
class LocalStorageUserContextServiceTest {

    @Mock
    private VortexCrudConfigService<String, String, String> configService;

    @Mock
    private ReflectionService<String> reflectionService;

    @Mock
    private Application<String, String, String> application;

    @Mock
    private IdentityAndAccessManagement<String, String, String> identityAndAccessManagement;

    @Mock
    @SuppressWarnings("rawtypes")
    private VortexCrudDataStore dataStore;

    @Mock
    private InternalFormElement<String> usernameElement;

    private LocalStorageUserContextService<String, String, String> userContextService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        userContextService = new LocalStorageUserContextService<>(configService, reflectionService);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        if (mocks != null) {
            mocks.close();
        }
    }

    // ========== resolveRolesForEntity() Tests ==========

    @Test
    void testResolveRolesForEntity_WithValidEntity() {
        // Setup
        Object userEntity = new Object();
        String rolesField = "roles";

        VortexCrudRoleProvider role1 = mock(VortexCrudRoleProvider.class);
        when(role1.getRole()).thenReturn("ROLE_ADMIN");

        VortexCrudRoleProvider role2 = mock(VortexCrudRoleProvider.class);
        when(role2.getRole()).thenReturn("ROLE_USER");

        List<VortexCrudRoleProvider> roles = List.of(role1, role2);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);

        List<SimpleGrantedAuthority> expectedRoles = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(identityAndAccessManagement.resolveRolesForEntity(any(), eq(userEntity))).thenReturn((List) expectedRoles);

        // Execute
        List<SimpleGrantedAuthority> result = userContextService.resolveRolesForEntity(userEntity);

        // Verify
        assertEquals(2, result.size(), "Should have 2 roles");
        assertTrue(result.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(result.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testResolveRolesForEntity_WithNullEntity() {
        // Execute
        List<SimpleGrantedAuthority> result = userContextService.resolveRolesForEntity(null);

        // Verify
        assertTrue(result.isEmpty(), "Should return empty list for null entity");
    }

    @Test
    void testResolveRolesForEntity_WithNullConfiguration() {
        // Setup
        Object userEntity = new Object();
        when(configService.configuration()).thenReturn(null);

        // Execute
        List<SimpleGrantedAuthority> result = userContextService.resolveRolesForEntity(userEntity);

        // Verify
        assertTrue(result.isEmpty(), "Should return empty list when configuration is null");
    }

    @Test
    void testResolveRolesForEntity_WithNoRoles() {
        // Setup
        Object userEntity = new Object();
        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.resolveRolesForEntity(any(), eq(userEntity))).thenReturn(Collections.emptyList());

        // Execute
        List<SimpleGrantedAuthority> result = userContextService.resolveRolesForEntity(userEntity);

        // Verify
        assertTrue(result.isEmpty(), "Should return empty list when no roles are resolved");
    }

    @Test
    void testResolveRolesForEntity_WithEmptyRolesList() {
        // Setup
        Object userEntity = new Object();

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.resolveRolesForEntity(any(), eq(userEntity))).thenReturn(Collections.emptyList());

        // Execute
        List<SimpleGrantedAuthority> result = userContextService.resolveRolesForEntity(userEntity);

        // Verify
        assertTrue(result.isEmpty(), "Should return empty list when resolved roles list is empty");
    }

    // ========== currentUserEntity() Tests ==========

    @Test
    @SuppressWarnings("unchecked")
    void testCurrentUserEntity_WithAuthenticatedUser() {
        // Setup authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Setup mocks
        Object expectedUser = new Object();
        String usernameField = "username";

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.dataStoreInstance()).thenReturn(dataStore);
        when(identityAndAccessManagement.username()).thenReturn(usernameElement);
        when(usernameElement.field()).thenReturn(usernameField);
        when(dataStore.getRecordsFromTableWhereColumnEquals(eq(usernameField), eq("testuser"), eq(0), eq(1)))
                .thenReturn(List.of(expectedUser));

        // Execute
        Object result = userContextService.currentUserEntity();

        // Verify
        assertEquals(expectedUser, result, "Should return the authenticated user entity");
    }

    @Test
    void testCurrentUserEntity_WithNullAuthentication() {
        // Setup
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);

        // Execute
        Object result = userContextService.currentUserEntity();

        // Verify
        assertNull(result, "Should return null when authentication is null");
    }

    @Test
    void testCurrentUserEntity_WithAnonymousUser() {
        // Setup
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "anonymousUser",
                null,
                Collections.emptyList()
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);

        // Execute
        Object result = userContextService.currentUserEntity();

        // Verify
        assertNull(result, "Should return null for anonymous user");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testCurrentUserEntity_WhenUserNotFoundInDataStore() {
        // Setup authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Setup mocks to return empty list (user not found)
        String usernameField = "username";

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.dataStoreInstance()).thenReturn(dataStore);
        when(identityAndAccessManagement.username()).thenReturn(usernameElement);
        when(usernameElement.field()).thenReturn(usernameField);
        when(dataStore.getRecordsFromTableWhereColumnEquals(eq(usernameField), eq("testuser"), eq(0), eq(1)))
                .thenReturn(Collections.emptyList());

        // Execute
        Object result = userContextService.currentUserEntity();

        // Verify
        assertNull(result, "Should return null when user is not found in data store");
    }

    @Test
    void testCurrentUserEntity_WhenExceptionOccurs() {
        // Setup authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Setup mocks to throw exception
        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.dataStoreInstance()).thenThrow(new RuntimeException("Test exception"));

        // Execute
        Object result = userContextService.currentUserEntity();

        // Verify
        assertNull(result, "Should return null when exception occurs");
    }

    // ========== currentUserRoles() Tests ==========

    @Test
    @SuppressWarnings("unchecked")
    void testCurrentUserRoles_WithAuthenticatedUserHavingRoles() {
        // Setup authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Setup user entity with roles
        Object userEntity = new Object();
        String usernameField = "username";

        List<SimpleGrantedAuthority> expectedRoles = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);
        when(identityAndAccessManagement.dataStoreInstance()).thenReturn(dataStore);
        when(identityAndAccessManagement.username()).thenReturn(usernameElement);
        when(usernameElement.field()).thenReturn(usernameField);
        when(dataStore.getRecordsFromTableWhereColumnEquals(eq(usernameField), eq("testuser"), eq(0), eq(1)))
                .thenReturn(List.of(userEntity));
        when(identityAndAccessManagement.resolveRolesForEntity(any(), eq(userEntity))).thenReturn((List) expectedRoles);

        // Execute
        Set<String> result = userContextService.currentUserRoles();

        // Verify
        assertEquals(2, result.size(), "Should have 2 roles");
        assertTrue(result.contains("ROLE_ADMIN"));
        assertTrue(result.contains("ROLE_USER"));
    }

    @Test
    void testCurrentUserRoles_WithNoAuthenticatedUser() {
        // Setup
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        when(configService.configuration()).thenReturn(application);
        when(application.identityAndAccessManagement()).thenReturn(identityAndAccessManagement);

        // Execute
        Set<String> result = userContextService.currentUserRoles();

        // Verify
        assertTrue(result.isEmpty(), "Should return empty set when no user is authenticated");
    }

}
