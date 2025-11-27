package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.security.core.service.LocalStorageUserContextService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalStorageVortexCrudRbacPermissionCheckerTest {

    @Mock
    private LocalStorageUserContextService<String, String, String> userContextService;

    @Mock
    private VortexCrudConfigService<String, String, String> crudConfigService;

    private LocalStorageVortexCrudRbacPermissionChecker<String, String, String> permissionChecker;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        permissionChecker = new LocalStorageVortexCrudRbacPermissionChecker<>(userContextService, crudConfigService);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear security context after each test
        SecurityContextHolder.clearContext();
        if (mocks != null) {
            mocks.close();
        }
    }

    // ========== isAuthenticated() Tests ==========

    @Test
    void testIsAuthenticated_WithAuthenticatedUser() {
        // Setup authenticated user
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertTrue(permissionChecker.isAuthenticated(), "User should be authenticated");
    }

    @Test
    void testIsAuthenticated_WithAnonymousUser() {
        // Setup anonymous user
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "anonymousUser",
                null,
                Collections.emptyList()
        );
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertFalse(permissionChecker.isAuthenticated(), "Anonymous user should not be authenticated");
    }

    @Test
    void testIsAuthenticated_WithNullAuthentication() {
        // Setup null authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertFalse(permissionChecker.isAuthenticated(), "Null authentication should return false");
    }

    // ========== currentUserEntity() Tests ==========

    @Test
    void testcurrentUserEntity_WithValidService() {
        Object userEntity = new Object();
        when(userContextService.currentUserEntity()).thenReturn(userEntity);

        Object result = permissionChecker.currentUserEntity();

        assertEquals(userEntity, result, "Should return user entity from service");
    }

    @Test
    void testcurrentUserEntity_WithNullService() {
        LocalStorageUserContextService<String, String, String> checker =
                new LocalStorageUserContextService<>(null, null);

        Object result = checker.currentUserEntity();

        assertNull(result, "Should return null when service is null");
    }

    // ========== hasUserWriteAccessToRoute() Tests ==========

    @Test
    void testHasUserWriteAccessToRoute_UserHasWriteRole() {
        // Setup user with admin role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin", "user"));

        // Setup route with admin write role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));

        assertTrue(permissionChecker.hasUserWriteAccessToRoute(route),
                "User with admin role should have write access");
    }

    @Test
    void testHasUserWriteAccessToRoute_UserDoesNotHaveWriteRole() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route with admin write role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));

        assertFalse(permissionChecker.hasUserWriteAccessToRoute(route),
                "User without admin role should not have write access");
    }

    @Test
    void testHasUserWriteAccessToRoute_NoWriteRolesDefined() {
        // Setup user with roles
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin"));

        // Setup route with no write roles
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(Collections.emptyList());

        assertFalse(permissionChecker.hasUserWriteAccessToRoute(route),
                "Should deny access when no write roles are defined");
    }

    @Test
    void testHasUserWriteAccessToRoute_UserHasNoRoles() {
        // Setup user with no roles
        when(userContextService.currentUserRoles()).thenReturn(Collections.emptySet());

        // Setup route with write roles
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));

        assertFalse(permissionChecker.hasUserWriteAccessToRoute(route),
                "User with no roles should not have write access");
    }

    @Test
    void testHasUserWriteAccessToRoute_NullResource() {
        assertFalse(permissionChecker.hasUserWriteAccessToRoute(null),
                "Should return false for null resource");
    }

    // ========== hasUserReadAccessToRoute() Tests ==========

    @Test
    void testHasUserReadAccessToRoute_UserHasWriteRole() {
        // Setup user with admin role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin"));

        // Setup route with admin write role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(Collections.emptyList());

        assertTrue(permissionChecker.hasUserReadAccessToRoute(route),
                "User with write role should have read access");
    }

    @Test
    void testHasUserReadAccessToRoute_UserHasReadOnlyRole() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route with viewer read-only role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(Collections.emptyList());
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        assertTrue(permissionChecker.hasUserReadAccessToRoute(route),
                "User with read-only role should have read access");
    }

    @Test
    void testHasUserReadAccessToRoute_UserHasBothRoles() {
        // Setup user with both admin and viewer roles
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin", "viewer"));

        // Setup route with both write and read-only roles
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        assertTrue(permissionChecker.hasUserReadAccessToRoute(route),
                "User with either write or read-only role should have read access");
    }

    @Test
    void testHasUserReadAccessToRoute_UserHasNoMatchingRole() {
        // Setup user with guest role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("guest"));

        // Setup route with admin and viewer roles
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        assertFalse(permissionChecker.hasUserReadAccessToRoute(route),
                "User without matching role should not have read access");
    }

    @Test
    void testHasUserReadAccessToRoute_NoRolesDefined() {
        // Setup user with roles
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin"));

        // Setup route with no roles defined
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(Collections.emptyList());
        when(route.readOnlyRoles()).thenReturn(Collections.emptyList());

        assertFalse(permissionChecker.hasUserReadAccessToRoute(route),
                "Should deny access when no roles are defined");
    }

    @Test
    void testHasUserReadAccessToRoute_NullResource() {
        assertFalse(permissionChecker.hasUserReadAccessToRoute(null),
                "Should return false for null resource");
    }

    // ========== getUserFieldAccess() Tests ==========

    @Test
    void testGetUserFieldAccess_NoRouteAccess() {
        // Setup user with guest role (no access to route)
        when(userContextService.currentUserRoles()).thenReturn(Set.of("guest"));

        // Setup route with admin write role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(Collections.emptyList());

        Field<String, String, String> field = mock(Field.class);

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE, result,
                "User without route access should have no field access");
    }

    @Test
    void testGetUserFieldAccess_InheritWriteFromRoute() {
        // Setup user with admin role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("admin"));

        // Setup route with admin write role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(Collections.emptyList());

        // Setup field with no specific permissions (inherits from route)
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(Collections.emptyList());
        when(field.readOnlyRoles()).thenReturn(Collections.emptyList());

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.WRITE, result,
                "User with route write access should inherit write access to field");
    }

    @Test
    void testGetUserFieldAccess_InheritReadOnlyFromRoute() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route with viewer read-only role
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(Collections.emptyList());
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        // Setup field with no specific permissions (inherits from route)
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(Collections.emptyList());
        when(field.readOnlyRoles()).thenReturn(Collections.emptyList());

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY, result,
                "User with route read-only access should inherit read-only access to field");
    }

    @Test
    void testGetUserFieldAccess_FieldWritePermission() {
        // Setup user with editor role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("editor"));

        // Setup route allowing editor
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("editor"));
        when(route.readOnlyRoles()).thenReturn(Collections.emptyList());

        // Setup field with editor write permission
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(List.of("editor"));
        when(field.readOnlyRoles()).thenReturn(Collections.emptyList());

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.WRITE, result,
                "User with field write permission should have write access");
    }

    @Test
    void testGetUserFieldAccess_FieldReadOnlyPermission() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route allowing viewer read-only
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        // Setup field with viewer read-only permission
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(Collections.emptyList());
        when(field.readOnlyRoles()).thenReturn(List.of("viewer"));

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY, result,
                "User with field read-only permission should have read-only access");
    }

    @Test
    void testGetUserFieldAccess_RouteAccessButNotFieldAccess() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route allowing viewer read-only
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(Collections.emptyList());
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        // Setup field with admin-only permission
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(List.of("admin"));
        when(field.readOnlyRoles()).thenReturn(Collections.emptyList());

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE, result,
                "User with route access but no field access should have no access to field");
    }

    @Test
    void testGetUserFieldAccess_FieldWriteButRouteReadOnly() {
        // Setup user with viewer role
        when(userContextService.currentUserRoles()).thenReturn(Set.of("viewer"));

        // Setup route allowing viewer read-only
        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        // Setup field with viewer write permission (field-level)
        Field<String, String, String> field = mock(Field.class);
        when(field.writeRoles()).thenReturn(List.of("viewer"));
        when(field.readOnlyRoles()).thenReturn(Collections.emptyList());

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.READ_ONLY, result,
                "User with field write permission but only route read access should have read-only access");
    }

    @Test
    void testGetUserFieldAccess_NullRoute() {
        Field<String, String, String> field = mock(Field.class);

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(null, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE, result,
                "Should return NONE for null route");
    }

    @Test
    void testGetUserFieldAccess_NullField() {
        AccessControlled route = mock(AccessControlled.class);

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, null);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE, result,
                "Should return NONE for null field");
    }

    @Test
    void testGetUserFieldAccess_UserHasNoRoles() {
        // Setup user with no roles
        when(userContextService.currentUserRoles()).thenReturn(Collections.emptySet());

        AccessControlled route = mock(AccessControlled.class);
        when(route.writeRoles()).thenReturn(List.of("admin"));
        when(route.readOnlyRoles()).thenReturn(List.of("viewer"));

        Field<String, String, String> field = mock(Field.class);

        VortexCrudRbacPermissionChecker.FieldAccessLevel result = permissionChecker.getUserFieldAccess(route, field);

        assertEquals(VortexCrudRbacPermissionChecker.FieldAccessLevel.NONE, result,
                "User with no roles should have no field access");
    }
}