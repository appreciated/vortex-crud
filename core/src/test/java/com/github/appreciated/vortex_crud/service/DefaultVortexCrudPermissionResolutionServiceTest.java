package com.github.appreciated.vortex_crud.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.ListRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.service.DefaultVortexCrudPermissionResolutionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultVortexCrudPermissionResolutionServiceTest {

    @Mock
    private VortexCrudConfigService<String, String, String> configService;

    @Mock
    private VortexCrudRouteFactoryRegistry<String, String, String> routeFactoryRegistry;

    @Mock
    private VortexCrudDataStoreUtilStrategy dataStoreUtil;

    @Mock
    private Application<String, String, String> application;

    private DefaultVortexCrudPermissionResolutionService<String, String, String> resolutionService;

    private Map<String, RouteRenderer<String, String, String>> routesConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        routesConfig = new HashMap<>();

        when(configService.configuration()).thenReturn(application);
        when(application.getRouteRenderers()).thenReturn(routesConfig);

        // Set up container factory mock
        TestContainerRouteFactory containerFactory = mock(TestContainerRouteFactory.class);
        when(containerFactory.isContainerRoute()).thenReturn(true);
        when(routeFactoryRegistry.getFactory(TestContainerRouteFactory.class)).thenReturn(containerFactory);

        // Set up non-container factory mock
        TestNonContainerRouteFactory nonContainerFactory = mock(TestNonContainerRouteFactory.class);
        when(nonContainerFactory.isContainerRoute()).thenReturn(false);
        when(routeFactoryRegistry.getFactory(TestNonContainerRouteFactory.class)).thenReturn(nonContainerFactory);

        resolutionService = new DefaultVortexCrudPermissionResolutionService<>(
                configService,
                routeFactoryRegistry,
                dataStoreUtil
        );
    }

    @Test
    void testResolveSimplePath() {
        // Setup simple route
        ListRoute<String, String, String> projectsRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Projects")
                .build();
        routesConfig.put("projects-cards", projectsRoute);

        // Test resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("projects-cards");

        assertNotNull(resolved, "Route should be resolved");
        assertEquals("Projects", resolved.title(), "Route title should match");
    }

    @Test
    void testResolveNestedPath() {
        // Setup nested route structure
        ListRoute<String, String, String> childRoute = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .title("Child Route")
                .build();

        ListRoute<String, String, String> parentRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Parent Route")
                .child(childRoute)
                .build();

        routesConfig.put("parent", parentRoute);

        // Test resolution - should resolve to parent (container route)
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("parent/123");

        assertNotNull(resolved, "Route should be resolved");
        assertEquals("Parent Route", resolved.title(), "Should resolve to parent container route");
    }

    @Test
    void testResolvePathWithLeadingSlash() {
        // Setup route
        ListRoute<String, String, String> tasksRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Tasks")
                .build();
        routesConfig.put("tasks", tasksRoute);

        // Test resolution with leading slash
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("/tasks");

        assertNotNull(resolved, "Route should be resolved even with leading slash");
        assertEquals("Tasks", resolved.title(), "Route title should match");
    }

    @Test
    void testResolveNullPath() {
        // Setup default route
        ListRoute<String, String, String> defaultRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Default Route")
                .isDefaultRoute(true)
                .build();
        routesConfig.put("default", defaultRoute);

        // Test null path resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath(null);

        assertNotNull(resolved, "Null path should resolve to default route");
        assertEquals("Default Route", resolved.title(), "Should resolve to default route");
    }

    @Test
    void testResolveEmptyPath() {
        // Setup default route
        ListRoute<String, String, String> defaultRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Default Route")
                .isDefaultRoute(true)
                .build();
        routesConfig.put("default", defaultRoute);

        // Test empty path resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("");

        assertNotNull(resolved, "Empty path should resolve to default route");
        assertEquals("Default Route", resolved.title(), "Should resolve to default route");
    }

    @Test
    void testResolveRootPath() {
        // Setup default route
        ListRoute<String, String, String> defaultRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Default Route")
                .isDefaultRoute(true)
                .build();
        routesConfig.put("default", defaultRoute);

        // Test root path resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("/");

        assertNotNull(resolved, "Root path should resolve to default route");
        assertEquals("Default Route", resolved.title(), "Should resolve to default route");
    }

    @Test
    void testResolveNonExistentPath() {
        // Don't add any routes to config

        // Test non-existent path resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("non-existent-route");

        assertNull(resolved, "Non-existent route should return null");
    }

    @Test
    void testResolvePathWhenNoDefaultRoute() {
        // Add routes but none marked as default
        ListRoute<String, String, String> route = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Some Route")
                .isDefaultRoute(false)
                .build();
        routesConfig.put("some-route", route);

        // Test null path when no default exists
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath(null);

        assertNull(resolved, "Should return null when no default route exists");
    }

    @Test
    void testResolveDeepNestedPath() {
        // Setup deeply nested route structure
        ListRoute<String, String, String> grandchildRoute = ListRoute.<String, String, String>builder()
                .factory(TestNonContainerRouteFactory.class)
                .title("Grandchild Route")
                .build();

        ListRoute<String, String, String> childRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Child Route")
                .child(grandchildRoute)
                .build();

        ListRoute<String, String, String> parentRoute = ListRoute.<String, String, String>builder()
                .factory(TestContainerRouteFactory.class)
                .title("Parent Route")
                .child(childRoute)
                .build();

        routesConfig.put("parent", parentRoute);

        // Test deep nested path resolution
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("parent/child/grandchild");

        assertNotNull(resolved, "Deep nested path should be resolved");
        // Based on VortexCrudPathToRouteResolver logic, should return parent (first container)
        assertEquals("Parent Route", resolved.title(), "Should resolve to parent container route");
    }

    @Test
    void testResolvePathWithNullConfigService() {
        // Create service with null config service
        DefaultVortexCrudPermissionResolutionService<String, String, String> nullConfigService =
                new DefaultVortexCrudPermissionResolutionService<>(null, routeFactoryRegistry, dataStoreUtil);

        // Test resolution with null config service
        RouteRenderer<String, String, String> resolved = nullConfigService.resolveRouteForPath("any-path");

        assertNull(resolved, "Should return null when config service is null");
    }

    @Test
    void testResolvePathWithNullConfiguration() {
        // Mock config service to return null configuration
        when(configService.configuration()).thenReturn(null);

        // Test resolution with null configuration
        RouteRenderer<String, String, String> resolved = resolutionService.resolveRouteForPath("any-path");

        assertNull(resolved, "Should return null when configuration is null");
    }
}