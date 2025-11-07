package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.security.RbacPermissionChecker.AccessLevel;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("RbacPermissionChecker Tests")
class LocalStorageRbacPermissionCheckerTest {

    private LocalStorageRbacPermissionChecker permissionChecker;
    private SecurityContext securityContext;
    private Authentication authentication;
    private VortexCrudConfigService<?, ?, ?> configService;
    private Application<?, ?, ?> application;
    private IdentityAndAccessManagement<?, ?, ?> identityAndAccessManagement;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        configService = mock(VortexCrudConfigService.class);
        application = mock(Application.class);
        identityAndAccessManagement = mock(IdentityAndAccessManagement.class);

        when(configService.configuration()).thenReturn((Application) application);
        when(application.identityAndAccessManagement()).thenReturn((IdentityAndAccessManagement) identityAndAccessManagement);

        permissionChecker = new LocalStorageRbacPermissionChecker(configService);
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ==================== getAccessLevel Tests ====================

    @Test
    @DisplayName("Should return WRITE when resource is null")
    void getAccessLevel_nullResource_returnsWrite() {
        AccessLevel result = permissionChecker.getAccessLevel(null);
        assertEquals(AccessLevel.WRITE, result);
    }

    @Test
    @DisplayName("Should return WRITE when no roles are specified")
    void getAccessLevel_noRolesSpecified_returnsWrite() {
        AccessControlled resource = createResource(null, null);
        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.WRITE, result);
    }

    @Test
    @DisplayName("Should return WRITE when user has matching write role")
    void getAccessLevel_userHasWriteRole_returnsWrite() {
        setupAuthentication(List.of("admin", "user"));
        AccessControlled resource = createResource(List.of("admin", "editor"), List.of("viewer"));

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.WRITE, result);
    }

    @Test
    @DisplayName("Should return READ_ONLY when user has matching read-only role")
    void getAccessLevel_userHasReadOnlyRole_returnsReadOnly() {
        setupAuthentication(List.of("viewer", "guest"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.READ_ONLY, result);
    }

    @Test
    @DisplayName("Should return NONE when user has no matching roles")
    void getAccessLevel_userHasNoMatchingRoles_returnsNone() {
        setupAuthentication(List.of("guest"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.NONE, result);
    }

    @Test
    @DisplayName("Should prioritize write access over read-only access")
    void getAccessLevel_userHasBothRoles_returnsWrite() {
        setupAuthentication(List.of("admin", "viewer"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.WRITE, result);
    }

    @Test
    @DisplayName("Should return NONE when user is not authenticated")
    void getAccessLevel_notAuthenticated_returnsNone() {
        when(securityContext.getAuthentication()).thenReturn(null);
        AccessControlled resource = createResource(List.of("admin"), null);

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.NONE, result);
    }

    @Test
    @DisplayName("Should handle ROLE_ prefix in granted authorities")
    void getAccessLevel_withRolePrefix_stripsPrefix() {
        setupAuthenticationWithPrefix(List.of("ROLE_admin", "ROLE_user"));
        AccessControlled resource = createResource(List.of("admin"), null);

        AccessLevel result = permissionChecker.getAccessLevel(resource);
        assertEquals(AccessLevel.WRITE, result);
    }

    // ==================== canWrite Tests ====================

    @Test
    @DisplayName("canWrite should return true for WRITE access")
    void canWrite_writeAccess_returnsTrue() {
        setupAuthentication(List.of("admin"));
        AccessControlled resource = createResource(List.of("admin"), null);

        assertTrue(permissionChecker.canWrite(resource));
    }

    @Test
    @DisplayName("canWrite should return false for READ_ONLY access")
    void canWrite_readOnlyAccess_returnsFalse() {
        setupAuthentication(List.of("viewer"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertFalse(permissionChecker.canWrite(resource));
    }

    @Test
    @DisplayName("canWrite should return false for NONE access")
    void canWrite_noAccess_returnsFalse() {
        setupAuthentication(List.of("guest"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertFalse(permissionChecker.canWrite(resource));
    }

    // ==================== canRead Tests ====================

    @Test
    @DisplayName("canRead should return true for WRITE access")
    void canRead_writeAccess_returnsTrue() {
        setupAuthentication(List.of("admin"));
        AccessControlled resource = createResource(List.of("admin"), null);

        assertTrue(permissionChecker.canRead(resource));
    }

    @Test
    @DisplayName("canRead should return true for READ_ONLY access")
    void canRead_readOnlyAccess_returnsTrue() {
        setupAuthentication(List.of("viewer"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertTrue(permissionChecker.canRead(resource));
    }

    @Test
    @DisplayName("canRead should return false for NONE access")
    void canRead_noAccess_returnsFalse() {
        setupAuthentication(List.of("guest"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertFalse(permissionChecker.canRead(resource));
    }

    // ==================== hasAccess Tests ====================

    @Test
    @DisplayName("hasAccess should return true for WRITE access")
    void hasAccess_writeAccess_returnsTrue() {
        setupAuthentication(List.of("admin"));
        AccessControlled resource = createResource(List.of("admin"), null);

        assertTrue(permissionChecker.hasAccess(resource));
    }

    @Test
    @DisplayName("hasAccess should return true for READ_ONLY access")
    void hasAccess_readOnlyAccess_returnsTrue() {
        setupAuthentication(List.of("viewer"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertTrue(permissionChecker.hasAccess(resource));
    }

    @Test
    @DisplayName("hasAccess should return false for NONE access")
    void hasAccess_noAccess_returnsFalse() {
        setupAuthentication(List.of("guest"));
        AccessControlled resource = createResource(List.of("admin"), List.of("viewer"));

        assertFalse(permissionChecker.hasAccess(resource));
    }

    // ==================== isAuthenticated Tests ====================

    @Test
    @DisplayName("isAuthenticated should return true when user is authenticated")
    void isAuthenticated_userAuthenticated_returnsTrue() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user");

        assertTrue(permissionChecker.isAuthenticated());
    }

    @Test
    @DisplayName("isAuthenticated should return false when authentication is null")
    void isAuthenticated_authenticationNull_returnsFalse() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertFalse(permissionChecker.isAuthenticated());
    }

    @Test
    @DisplayName("isAuthenticated should return false when user is not authenticated")
    void isAuthenticated_notAuthenticated_returnsFalse() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertFalse(permissionChecker.isAuthenticated());
    }

    @Test
    @DisplayName("isAuthenticated should return false for anonymousUser")
    void isAuthenticated_anonymousUser_returnsFalse() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        assertFalse(permissionChecker.isAuthenticated());
    }

    // ==================== getCurrentUserRoles Tests ====================

    @Test
    @DisplayName("getCurrentUserRoles should return roles from authentication")
    void getCurrentUserRoles_authenticated_returnsRoles() {
        setupAuthentication(List.of("admin", "user"));

        Set<String> roles = permissionChecker.getCurrentUserRoles();
        assertEquals(2, roles.size());
        assertTrue(roles.contains("admin"));
        assertTrue(roles.contains("user"));
    }

    @Test
    @DisplayName("getCurrentUserRoles should return empty set when not authenticated")
    void getCurrentUserRoles_notAuthenticated_returnsEmptySet() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Set<String> roles = permissionChecker.getCurrentUserRoles();
        assertTrue(roles.isEmpty());
    }

    @Test
    @DisplayName("getCurrentUserRoles should strip ROLE_ prefix")
    void getCurrentUserRoles_withPrefix_stripsPrefix() {
        setupAuthenticationWithPrefix(List.of("ROLE_admin", "ROLE_user"));

        Set<String> roles = permissionChecker.getCurrentUserRoles();
        assertEquals(2, roles.size());
        assertTrue(roles.contains("admin"));
        assertTrue(roles.contains("user"));
        assertFalse(roles.contains("ROLE_admin"));
    }

    @Test
    @DisplayName("getCurrentUserRoles should handle mixed prefix and non-prefix roles")
    void getCurrentUserRoles_mixedPrefix_handlesBoth() {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_admin"),
                new SimpleGrantedAuthority("user"),
                new SimpleGrantedAuthority("ROLE_editor")
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn((java.util.Collection) authorities);

        Set<String> roles = permissionChecker.getCurrentUserRoles();
        assertEquals(3, roles.size());
        assertTrue(roles.contains("admin"));
        assertTrue(roles.contains("user"));
        assertTrue(roles.contains("editor"));
    }

    // ==================== currentUserHasRequiredRoles Tests ====================

    @Test
    @DisplayName("currentUserHasRequiredRoles should return true when user has the role")
    void currentUserHasRequiredRoles_userHasRequiredRoles_returnsTrue() {
        setupAuthentication(List.of("admin", "user"));

        assertTrue(permissionChecker.currentUserHasRequiredRoles("admin"));
        assertTrue(permissionChecker.currentUserHasRequiredRoles("user"));
    }

    @Test
    @DisplayName("currentUserHasRequiredRoles should return false when user does not have the role")
    void currentUserHasRequiredRoles_userDoesNotHaveRequiredRoles_returnsFalse() {
        setupAuthentication(List.of("user"));

        assertFalse(permissionChecker.currentUserHasRequiredRoles("admin"));
    }

    @Test
    @DisplayName("currentUserHasRequiredRoles should return false when role is null")
    void currentUserHasRequiredRoles_nullRequiredRoles_returnsFalse() {
        setupAuthentication(List.of("admin"));

        assertFalse(permissionChecker.currentUserHasRequiredRoles(null));
    }

    @Test
    @DisplayName("currentUserHasRequiredRoles should return false when not authenticated")
    void currentUserHasRequiredRoles_notAuthenticated_returnsFalse() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertFalse(permissionChecker.currentUserHasRequiredRoles("admin"));
    }

    // ==================== getCurrentUserEntity Tests ====================

    @Test
    @DisplayName("getCurrentUserEntity should return user entity when IAM configured")
    @SuppressWarnings("unchecked")
    void getCurrentUserEntity_iamConfigured_returnsUserEntity() {
        Object mockUserEntity = new Object();
        when(((IdentityAndAccessManagement<Object, Object, Object>) identityAndAccessManagement).getCurrentUserEntity()).thenReturn(mockUserEntity);

        Object result = permissionChecker.getCurrentUserEntity();
        assertEquals(mockUserEntity, result);
    }

    @Test
    @DisplayName("getCurrentUserEntity should return null when IAM not configured")
    void getCurrentUserEntity_iamNotConfigured_returnsNull() {
        when(application.identityAndAccessManagement()).thenReturn(null);

        Object result = permissionChecker.getCurrentUserEntity();
        assertNull(result);
    }

    @Test
    @DisplayName("getCurrentUserEntity should return null when config service is null")
    void getCurrentUserEntity_configServiceNull_returnsNull() {
        LocalStorageRbacPermissionChecker checkerWithoutConfig = new LocalStorageRbacPermissionChecker(null);

        Object result = checkerWithoutConfig.getCurrentUserEntity();
        assertNull(result);
    }

    @Test
    @DisplayName("getCurrentUserEntity should return null when IAM returns null")
    @SuppressWarnings("unchecked")
    void getCurrentUserEntity_iamReturnsNull_returnsNull() {
        when(((IdentityAndAccessManagement<Object, Object, Object>) identityAndAccessManagement).getCurrentUserEntity()).thenReturn(null);

        Object result = permissionChecker.getCurrentUserEntity();
        assertNull(result);
    }

    // ==================== Helper Methods ====================

    private AccessControlled createResource(List<String> writeRoles, List<String> readOnlyRoles) {
        return new AccessControlled() {
            @Override
            public List<String> writeRoles() {
                return writeRoles;
            }

            @Override
            public List<String> readOnlyRoles() {
                return readOnlyRoles;
            }
        };
    }

    private void setupAuthentication(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .map(auth -> (GrantedAuthority) auth)
                .toList();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn((java.util.Collection) authorities);
    }

    private void setupAuthenticationWithPrefix(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .map(auth -> (GrantedAuthority) auth)
                .toList();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getAuthorities()).thenReturn((java.util.Collection) authorities);
    }
}