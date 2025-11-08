package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudPermissionResolutionService;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.server.auth.NavigationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class VortexCrudNavigationAccessCheckerTest {

    @Mock
    private VortexCrudConfigService<String, String, String> configService;

    @Mock
    private LocalStorageRbacPermissionChecker<String, String, String> permissionChecker;

    @Mock
    private VortexCrudPermissionResolutionService<String, String, String> resolutionService;

    @Mock
    private Application<String, String, String> application;

    @Mock
    private NavigationContext context;

    @Mock
    private Location location;

    private VortexCrudNavigationAccessChecker<String, String, String> accessChecker;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        when(configService.configuration()).thenReturn(application);
        when(context.getLocation()).thenReturn(location);

        accessChecker = new VortexCrudNavigationAccessChecker<>(configService, permissionChecker, resolutionService);
    }

    // ========== Public Routes Tests ==========

    @Test
    void testCheck_LoginRoute_AllowsAccess() {
        when(location.getPath()).thenReturn("login");

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
        verifyNoInteractions(permissionChecker, resolutionService);
    }

    @Test
    void testCheck_SignUpRoute_AllowsAccess() {
        when(location.getPath()).thenReturn("sign-up");

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
        verifyNoInteractions(permissionChecker, resolutionService);
    }

    @Test
    void testCheck_AccessDeniedRoute_AllowsAccess() {
        when(location.getPath()).thenReturn("access-denied");

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
        verifyNoInteractions(permissionChecker, resolutionService);
    }

    // ========== IAM Disabled Tests ==========

    @Test
    void testCheck_IAMDisabled_AllowsAllRoutes() {
        when(location.getPath()).thenReturn("some-route");
        when(application.identityAndAccessManagement()).thenReturn(null);

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
        verifyNoInteractions(permissionChecker, resolutionService);
    }

    // ========== Authentication Tests ==========

    @Test
    void testCheck_UserNotAuthenticated_DeniesAccess() {
        when(location.getPath()).thenReturn("projects");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(false);

        accessChecker.check(context);

        verify(context).deny("Not authenticated");
        verify(context, never()).allow();
        verifyNoInteractions(resolutionService);
    }

    // ========== Route Resolution Tests ==========

    @Test
    void testCheck_RouteNotFoundInConfig_AllowsAccess() {
        when(location.getPath()).thenReturn("non-vortex-route");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath("non-vortex-route")).thenReturn(null);

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
    }

    // ========== Permission Check Tests ==========

    @Test
    void testCheck_UserHasReadAccess_AllowsAccess() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);

        when(location.getPath()).thenReturn("projects");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath("projects")).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(true);

        accessChecker.check(context);

        verify(context).allow();
        verify(context, never()).deny(anyString());
    }

    @Test
    void testCheck_UserHasNoAccess_DeniesAccess() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);

        when(location.getPath()).thenReturn("admin-panel");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath("admin-panel")).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(false);

        accessChecker.check(context);

        verify(context).deny("Insufficient permissions");
        verify(context, never()).allow();
    }

    // ========== Nested Route Tests ==========

    @Test
    void testCheck_NestedRoute_UsesResolutionService() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);
        String nestedPath = "projects-cards/123";

        when(location.getPath()).thenReturn(nestedPath);
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath(nestedPath)).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(true);

        accessChecker.check(context);

        verify(resolutionService).resolveRouteForPath(nestedPath);
        verify(context).allow();
    }

    @Test
    void testCheck_DeeplyNestedRoute_UsesResolutionService() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);
        String deepNestedPath = "submenu/project-form/456/details";

        when(location.getPath()).thenReturn(deepNestedPath);
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath(deepNestedPath)).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(true);

        accessChecker.check(context);

        verify(resolutionService).resolveRouteForPath(deepNestedPath);
        verify(context).allow();
    }

    // ========== Edge Cases ==========

    @Test
    void testCheck_EmptyPath_HandlesProperly() {
        when(location.getPath()).thenReturn("");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath("")).thenReturn(null);

        accessChecker.check(context);

        verify(context).allow();
    }

    @Test
    void testCheck_RootPath_HandlesProperly() {
        when(location.getPath()).thenReturn("/");
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath("/")).thenReturn(null);

        accessChecker.check(context);

        verify(context).allow();
    }

    @Test
    void testCheck_NullConfigService_AllowsAccess() {
        VortexCrudNavigationAccessChecker<String, String, String> checkerWithNullConfig =
                new VortexCrudNavigationAccessChecker<>(null, permissionChecker, resolutionService);

        when(location.getPath()).thenReturn("some-route");

        checkerWithNullConfig.check(context);

        verify(context).allow();
    }

    @Test
    void testCheck_NullConfiguration_AllowsAccess() {
        when(configService.configuration()).thenReturn(null);
        when(location.getPath()).thenReturn("some-route");

        accessChecker.check(context);

        verify(context).allow();
    }

    // ========== Integration Flow Tests ==========

    @Test
    void testCheck_FullAuthenticatedFlow_WithAccess() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);
        String path = "projects-cards/123";

        when(location.getPath()).thenReturn(path);
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath(path)).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(true);

        accessChecker.check(context);

        verify(permissionChecker).isAuthenticated();
        verify(resolutionService).resolveRouteForPath(path);
        verify(permissionChecker).hasUserReadAccessToRoute(route);
        verify(context).allow();
        verify(context, never()).deny(anyString());
    }

    @Test
    void testCheck_FullAuthenticatedFlow_WithoutAccess() {
        RouteRenderer<String, String, String> route = mock(RouteRenderer.class);
        String path = "admin-panel";

        when(location.getPath()).thenReturn(path);
        when(application.identityAndAccessManagement()).thenReturn(mock(com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement.class));
        when(permissionChecker.isAuthenticated()).thenReturn(true);
        when(resolutionService.resolveRouteForPath(path)).thenReturn(route);
        when(permissionChecker.hasUserReadAccessToRoute(route)).thenReturn(false);

        accessChecker.check(context);

        verify(permissionChecker).isAuthenticated();
        verify(resolutionService).resolveRouteForPath(path);
        verify(permissionChecker).hasUserReadAccessToRoute(route);
        verify(context).deny("Insufficient permissions");
        verify(context, never()).allow();
    }
}